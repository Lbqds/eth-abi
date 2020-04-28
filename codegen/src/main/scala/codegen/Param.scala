package codegen

import scala.meta.{Type, Term}
import fastparse._
import NoWhitespace._

// we assume that Tuple0 does not exist
final case class Param(name: String, `type`: String, components: Option[Seq[Param]] = None, indexed: Option[Boolean] = None) {
  private val canonical = {
    if (`type` == "uint") "uint256"
    else if (`type` == "int") "int256"
    else `type`
  }
  private [codegen] val tpe: Type = parseType()
  private [codegen] val termName: Term.Name = if (name.isEmpty) Term.fresh() else Term.Name(name)
  private [codegen] val signature: String = {
    if (canonical.startsWith("tuple")) {
      val postfix = canonical.slice(5, `type`.length)
      "(" + components.get.map(_.signature).mkString(",") + ")" + postfix
    } else {
      canonical
    }
  }
  private [codegen] def isTupleTpe: Boolean = components.isDefined && components.get.nonEmpty

  private [codegen] def isIndexed: Boolean = indexed.isDefined && indexed.get

  private def parseType(): Type = {
    def number[_ : P] = P(CharIn("0-9").rep.?.!.map(str => {
      if (str.isEmpty) 0 // dynamic array
      else str.toInt     // static array
    }))

    def uint[_ : P] = P("uint" ~ CharIn("0-9").rep.?)
    def int[_ : P] = P("int" ~ CharIn("0-9").rep.?)
    def address[_ : P] = P("address")
    def bool[_ : P] = P("bool")
    def string[_ : P] = P("string")
    def bytes[_ : P] = P("bytes" ~ CharIn("0-9").rep.?)
    def tuple[_ : P] = P("tuple")
    def baseType[_ : P] = P(uint | int | address | bool | string | bytes | tuple)
    def array[_ : P] = P(("[" ~ number ~ "]").rep.?)
    def t[_ : P] = P(baseType.! ~ array.?)

    parse(`type`, t(_)) match {
      case Parsed.Success((base, arr), _) =>
        val baseTypeName = typeName(base)
        parseWithArray(baseTypeName, arr.get.get)
      case failure: Parsed.Failure => throw new RuntimeException(s"parse type failed: $failure")
    }
  }

  private def parseWithArray(baseTypeName: String, arr: Seq[Int]): Type = {
    val baseTpe = baseTypeName match {
      case _ if baseTypeName == "TupleType" =>
        assert(components.isDefined && components.get.nonEmpty)
        val componentTypes = components.get.map(_.parseType())
        val tupleType = Type.Name(s"TupleType${componentTypes.length}")
        Type.Apply(tupleType, componentTypes.toList)
      case _ => Type.Name(baseTypeName)
    }
    val dynamicArrayTpe = Type.Name("DynamicArray")
    val staticArrayTpe = Type.Name("StaticArray")
    arr.foldLeft(baseTpe)((acc, length) => {
      if (length == 0) Type.Apply(dynamicArrayTpe, List(acc))
      else Type.Apply(staticArrayTpe, List(acc))
    })
  }

  private def typeName(base: String): String = {
    base match {
      case _ if base == "uint" => "uint256".capitalize
      case _ if base == "int" => "int256".capitalize
      case _ if base == "bytes" => "DynamicBytes"
      case _ if base == "string" => "StringType"
      case _ if base == "bool" => "Bool"
      case _ if base == "tuple" => "TupleType"
      case n => n.capitalize
    }
  }
}

object Param {
  import io.circe.jawn.decode
  import io.circe.generic.auto._
  def apply(json: String): Param = decode[Param](json).getOrElse(throw new RuntimeException("invalid param format"))
}