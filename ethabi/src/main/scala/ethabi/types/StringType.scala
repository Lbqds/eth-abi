package ethabi.types

import java.nio.charset.StandardCharsets

final class StringType(val value: String) extends SolType {
  override def toString: String = value
}

object StringType {
  def apply(value: String): StringType = new StringType(value)
  def from(value: String): StringType = StringType(value)

  implicit lazy val typeInfo: TypeInfo[StringType] = new TypeInfo[StringType] {
    override def name: String = "string"
    override def isStatic: Boolean = false
    override def encode[U >: StringType](value: U): Array[Byte] = {
      val bytes = value.asInstanceOf[StringType].value.getBytes(StandardCharsets.UTF_8)
      TypeInfo[DynamicBytes].encode(DynamicBytes(bytes))
    }
    override def decode(bytes: Array[Byte], position: Int): (StringType, Int) = {
      val (result, consumed) = TypeInfo[DynamicBytes].decode(bytes, position)
      (StringType(new String(result.value, StandardCharsets.UTF_8)), consumed)
    }
  }
}
