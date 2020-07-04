package codegen

import io.circe.jawn.decode
import io.circe.generic.auto._
import scala.meta._
import ethabi.util._

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
    q"""def deploy(..${args :+ sender :+ opt}): F[Deferred[F, Hash]] = {
      ..${encodeStat.stats}
      impl.deploy(CallArgs(encoded, sender, opt))
     }"""
  }

  private def callAndDecodeStat(retTpe: Option[Type]) = {
    if (retTpe.isDefined) {
      Term.Block(List(
        q"""
          for {
            promise <- impl.call(CallArgs(encoded, sender, opt))
            data    <- promise.get
          } yield {
            val result = ${decodeParams(retTpe.get, Term.Name("data"))}
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
    val returnType = retTpe.fold[Type](t"F[Deferred[F, Array[Byte]]]")(t => t"F[$t]")
    q"""
        def ${Term.Name(name.get)}(..${args :+ sender :+ opt}): $returnType = {
          ..${body.stats}
        }
     """
  }

  private def genTransactionFunction(): Defn = {
    val (args, encodeStat) = funcArgsAndEncodeStat
    q"""
        def ${Term.Name(name.get)}(..${args :+ sender :+ opt}): $defaultRetTpe = {
          ..${encodeStat.stats}
          impl.sendTransaction(CallArgs(encoded, sender, opt))
        }
     """
  }

  private [codegen] def genFunction: Defn = {
    assert(isFunction && name.isDefined)
    if (isConstant) genConstantFunction(returnType)
    else genTransactionFunction()
  }

  // FIXME: only support indexed event first, then non-indexed event
  private [codegen] def genEventDecodeFunc: Defn.Def = {
    assert(isEvent && !isAnonymous && name.isDefined)

    var decls = Seq.empty[String]
    def nextName: Term.Name = {
      val name = Term.fresh("typeInfo")
      decls = decls :+ name.value
      name
    }

    val (indexedTypes, nonIndexedTypes) = inputs.toList.flatten.partition(_.isIndexed)
    val indexedTypeInfos = indexedTypes.filter(_.isIndexed).map(p => q"""val ${Pat.Var(nextName)} = TypeInfo[${p.tpe}]""")
    val nonIndexTypeInfo = if (nonIndexedTypes.nonEmpty) {
      val tupleType = Type.Name(s"TupleType${nonIndexedTypes.length}")
      Some(q"""val ${Pat.Var(nextName)} = TypeInfo[${Type.Apply(tupleType, nonIndexedTypes.map(_.tpe))}]""")
    } else None

    val typeInfoDecls: List[Stat] = indexedTypeInfos ++ nonIndexTypeInfo.toList
    val declCodeString = s"""val typeInfos: List[TypeInfo[SolType]] = List${decls.mkString("(", ", ", ")")}"""
    val typeInfoDecl: Stat = declCodeString.parse[Stat].get
    val methodName = Term.Name(s"decode${name.get.capitalize}")

    q"""
        private def $methodName($log): Event = {
          ..$typeInfoDecls
          $typeInfoDecl
          Event.decode(typeInfos, log)
        }
     """
  }

  private [codegen] def genSubscribeEventFunc: Defn.Def = {
    assert(isEvent && !isAnonymous && name.isDefined)
    val funcName = Term.Name(s"subscribe${name.get.capitalize}")
    val decodeFunc = Term.Name(s"decode${name.get.capitalize}")
    val topic = signatureHash.get.toString
    q"""
        def $funcName: F[SubscriptionResult[F, Event]] = {
          for {
            result <- impl.subscribeLogs(Bytes32.from(${Lit.String(topic)}))
          } yield SubscriptionResult[F, Event](result.id, result.stream.map($decodeFunc))
        }
     """
  }

  private [codegen] def genEvent: List[Defn] = List(genEventDecodeFunc, genSubscribeEventFunc)
}

object AbiDefinition {
  private val defaultRetTpe = t"F[Deferred[F, Hash]]"
  private val opt = Term.Param(List.empty, Term.Name("opt"), Some(Type.Name("TransactionOpt")), None)
  private val sender = Term.Param(List.empty, Term.Name("sender"), Some(Type.Name("Address")), None)
  private val log = Term.Param(List.empty, Term.Name("log"), Some(Type.Name("Log")), None)

  def apply(json: String): AbiDefinition = decode[AbiDefinition](json).getOrElse(throw new RuntimeException("invalid abi format"))

  private def paramsToTuple(params: Seq[Param]): Type = {
    val paramTypes = params.map(_.tpe)
    val tupleType = Type.Name(s"TupleType${paramTypes.length}")
    Type.Apply(tupleType, paramTypes.toList)
  }

  private def encodeParams(paramsTpe: List[Type], params: List[Term.Param], inputs: List[Term.Name]): Term.Apply = {
    val name = s"TupleType${params.length}"
    val typeName = Type.Name(name)
    val termName = Term.Name(name)
    val applyFunc = Term.Select(termName, Term.Name("apply"))
    val bundle = Term.Apply(Term.ApplyType(applyFunc, params.map(_.decltpe.get)), inputs)
    val encodeFunc = Term.Select(q"TypeInfo[${Type.Apply(typeName, paramsTpe)}]", Term.Name("encode"))
    Term.Apply(encodeFunc, List(bundle))
  }

  private def decodeParams(paramsTpe: Type, input: Term.Name): Term.Apply = {
    val decodeFunc = Term.Select(q"TypeInfo[$paramsTpe]", Term.Name("decode"))
    Term.Apply(decodeFunc, List(input, Lit.Int(0)))
  }

  private def peel[Coll <: Iterable[_]](coll: Option[Coll]): Option[Coll] = {
    coll match {
      case Some(c) if c.isEmpty => None
      case c => c
    }
  }
}
