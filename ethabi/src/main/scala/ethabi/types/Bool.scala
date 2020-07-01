package ethabi.types

import generated.Uint8

final case class Bool(value: Boolean) extends SolType {
  override def toString = value.toString
}

object Bool {
  def from(value: String): Bool = value match {
    case "true" => Bool(true)
    case "false" => Bool(false)
    case _ => throw new RuntimeException("only construct Bool from `true` or `false`")
  }

  implicit lazy val typeInfo: TypeInfo[Bool] = new TypeInfo[Bool] {
    override def name: String = "bool"
    override def isStatic: Boolean = true
    override def encode[U >: Bool](value: U): Array[Byte] = {
      val b = value.asInstanceOf[Bool].value
      if (b) TypeInfo[Uint8].encode(Uint8(BigInt(1)))
      else TypeInfo[Uint8].encode(Uint8(BigInt(0)))
    }
    override def decode(bytes: Array[Byte], position: Int): (Bool, Int) = {
      val (result, consumed) = Uint8.typeInfo.decode(bytes, position)
      if (result.value.toInt == 1) (Bool(true), consumed)
      else (Bool(false), consumed)
    }
  }
}
