package codegen

import io.circe.jawn.decode
import io.circe.generic.auto._
import scala.meta._
import ethabi.util.{Hash, Hex}

// fallback function have no name and inputs
final case class AbiDefinition(`type`: String, name: Option[String], inputs: Option[Seq[Param]], outputs: Option[Seq[Param]],
                         stateMutability: Option[String], anonymous: Option[Boolean]) {
  import AbiDefinition._
  def isPayable: Boolean = stateMutability.isDefined && stateMutability.get == "payable"
  def isConstant: Boolean = stateMutability.isDefined && (stateMutability.get == "pure" || stateMutability.get == "view")
  def isEvent: Boolean = `type` == "event"
  def isFunction: Boolean = `type` == "function"
  def isConstructor: Boolean = `type` == "constructor"
  def isFallback: Boolean = `type` == "fallback"
  def isAnonymous: Boolean = anonymous.isDefined && anonymous.get

  private [codegen] val signature =
    if (isFunction || isEvent) name.map(_ + "(" + inputs.map(_.map(_.signature).mkString(",")).getOrElse("") + ")")
    else None
  private [codegen] val signatureHash = signature.map(sig => Hash.hash(sig.getBytes))
  private [codegen] val identifier = signatureHash.map(_.bytes.slice(0, 4))
  private val args = peel(inputs.map(_.map(p => Term.Param(List.empty, p.termName, Some(p.tpe), None)).toList))
  private val argsTpe = peel(inputs.map(_.map(_.tpe).toList))

  private val returnType: Option[Type] = {
    outputs match {
      case None => None
      case Some(params) if params.isEmpty => None
      case Some(params) => Some(paramsToTuple(params))
    }
  }

  private def funcArgsAndEncodeStat = {
    val id = identifier.map(id => Hex.bytes2Hex(id)).getOrElse("")
    val defaultEncodeStat = Term.Block(List(q"val encoded = Hex.hex2Bytes(${Lit.String(id)})"))
    val encodeStat = for {
      params <- args
      paramsTpe <- argsTpe
    } yield {
      Term.Block(List(
        q"val paramsEncoded = ${encodeParams(paramsTpe, params, params.map(_.name.asInstanceOf[Term.Name]))}",
        q"val functionId = Hex.hex2Bytes(${Lit.String(id)})",
        q"val encoded = functionId ++ paramsEncoded",
      ))
    }
    (args.getOrElse(List.empty), encodeStat.getOrElse(defaultEncodeStat))
  }

  private def ctorArgsAndEncodeStat = {
    val defaultEncodeStat = Term.Block(List(q"val encoded = Hex.hex2Bytes(binary)"))
    val encodeStat = for {
      params <- args
      paramsTpe <- argsTpe
    } yield {
      Term.Block(List(
        q"val paramsEncoded = ${encodeParams(paramsTpe, params, params.map(_.name.asInstanceOf[Term.Name]))}",
        q"val code = Hex.hex2Bytes(binary)",
        q"val encoded = code ++ paramsEncoded"
      ))
    }
    (args.getOrElse(List.empty), encodeStat.getOrElse(defaultEncodeStat))
  }

  private [codegen] def genConstructor: Defn = {
    assert(isConstructor)
    val (args, encodeStat) = ctorArgsAndEncodeStat
    val body = Term.Block(encodeStat.stats :+ q"impl.deploy(encoded, sender, opt)")
    Defn.Def(List.empty, Term.Name("deploy"), List.empty, List(args :+ sender :+ opt), Some(Type.Name("Unit")), body)
  }

  private def callAndDecodeStat(retTpe: Option[Type]) = {
    if (retTpe.isDefined) {
      Term.Block(List(
        q"""
          impl.call(encoded, sender, opt).map { bytes =>
            val result = ${decodeParams(retTpe.get, Term.Name("bytes"))}
            result._1
          }
         """
      ))
    } else {
      Term.Block(List(
        q"impl.call(encoded, sender, opt)"
      ))
    }
  }

  private def genConstantFunction(retTpe: Option[Type]): Defn = {
    val (args, encodeStat) = funcArgsAndEncodeStat
    val body = Term.Block(encodeStat.stats ++ callAndDecodeStat(retTpe).stats)
    Defn.Def(List.empty, Term.Name(name.get), List.empty, List(args :+ sender :+ opt), retTpe.map(t => Type.Apply(Type.Name("Future"), List(t))), body)
  }

  private def genTransactionFunction(retTpe: Option[Type]): Defn = {
    val (args, encodeStat) = funcArgsAndEncodeStat
    val body = Term.Block(encodeStat.stats :+ q"impl.sendTransaction(encoded, sender, opt)")
    Defn.Def(List.empty, Term.Name(name.get), List.empty, List(args :+ sender :+ opt), retTpe, body)
  }

  private [codegen] def genFunction: Defn = {
    assert(isFunction && name.isDefined)
    if (isConstant) genConstantFunction(returnType)
    else genTransactionFunction(Some(defaultRetTpe))
  }

  // FIXME: only support indexed event first, then non-indexed event
  private [codegen] def genEventDecodeFunc: Defn.Def = {
    assert(isEvent && !isAnonymous && name.isDefined)
    val typeInfosDecl = q"""var typeInfos = Seq.empty[TypeInfo[SolType]]"""
    val indexedTypeInfos = inputs.map(_.filter(_.isIndexed).map(p => q"""typeInfos = typeInfos :+ implicitly[TypeInfo[${p.tpe}]]"""))
    val nonIndexTypeInfo = inputs.flatMap { params =>
      val tpes = params.filter(!_.isIndexed).map(_.tpe).toList
      if (tpes.nonEmpty) {
        val tupleType = Type.Name(s"TupleType${tpes.length}")
        Some(q"""typeInfos = typeInfos :+ implicitly[TypeInfo[${Type.Apply(tupleType, tpes)}]]""")
      } else None
    }
    var stats: List[Stat] = List(typeInfosDecl)
    if (indexedTypeInfos.isDefined) stats = stats ++ indexedTypeInfos.get
    if (nonIndexTypeInfo.isDefined) stats = stats :+ nonIndexTypeInfo.get
    stats = stats :+ q"""EventValue.decodeEvent(typeInfos, log)"""
    Defn.Def(List.empty, Term.Name(s"decode${name.get.capitalize}"), List.empty, List(List(log)), Some(Type.Name("EventValue")), Term.Block(stats))
  }

  private [codegen] def genSubscribeEventFunc: Defn.Def = {
    assert(isEvent && !isAnonymous && name.isDefined)
    val funcName = Term.Name(s"subscribe${name.get.capitalize}")
    val decodeFunc = Term.Name(s"decode${name.get.capitalize}")
    val topic = signatureHash.get.toString
    q"""
       def $funcName: Source[EventValue, NotUsed] = {
         val query = LogQuery.from(contractAddress, Hash(${Lit.String(topic)}))
         impl.subscribeLogs(query).map($decodeFunc)
       }
     """
  }

  private [codegen] def genEvent: List[Defn] = List(genEventDecodeFunc, genSubscribeEventFunc)
}

object AbiDefinition {
  private val defaultRetTpe = Type.Apply(Type.Name("Future"), List(Type.Name("Hash")))
  private val opt = Term.Param(List.empty, Term.Name("opt"), Some(Type.Name("TransactionOpt")), None)
  private val sender = Term.Param(List.empty, Term.Name("sender"), Some(Type.Name("Address")), None)
  private val log = Term.Param(List.empty, Term.Name("log"), Some(Type.Name("Log")), None)

  def apply(json: String): AbiDefinition = decode[AbiDefinition](json).getOrElse(throw new RuntimeException("invalid abi format"))

  private def paramsToTuple(params: Seq[Param]): Type = {
    if (params.length == 1) params.head.tpe
    else {
      val paramTypes = params.map(_.tpe)
      val tupleType = Type.Name(s"TupleType${paramTypes.length}")
      Type.Apply(tupleType, paramTypes.toList)
    }
  }

  private def encodeParams(paramsTpe: List[Type], params: List[Term.Param], inputs: List[Term.Name]): Term.Apply = {
    val name = s"TupleType${params.length}"
    val typeName = Type.Name(name)
    val termName = Term.Name(name)
    val applyFunc = Term.Select(termName, Term.Name("apply"))
    val bundle = Term.Apply(Term.ApplyType(applyFunc, params.map(_.decltpe.get)), inputs)
    val typeInfo = q"implicitly[TypeInfo[${Type.Apply(typeName, paramsTpe)}]]"
    val encodeFunc = Term.Select(typeInfo, Term.Name("encode"))
    Term.Apply(encodeFunc, List(bundle))
  }

  private def decodeParams(paramsTpe: Type, input: Term.Name): Term.Apply = {
    val typeInfo = q"implicitly[TypeInfo[$paramsTpe]]"
    val decodeFunc = Term.Select(typeInfo, Term.Name("decode"))
    Term.Apply(decodeFunc, List(input, Lit.Int(0)))
  }

  private def peel[Coll <: Iterable[_]](coll: Option[Coll]): Option[Coll] = {
    coll match {
      case Some(c) if c.isEmpty => None
      case c => c
    }
  }
}
