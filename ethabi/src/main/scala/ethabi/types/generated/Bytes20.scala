// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex

final class Bytes20(val value: Array[Byte]) extends SolType {
  assert(value.length <= 20)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}

object Bytes20 {
  def apply(value: Array[Byte]): Bytes20 = new Bytes20(value)
  def from(value: String): Bytes20 = new Bytes20(Hex.hex2Bytes(value))

  implicit lazy val typeInfo: TypeInfo[Bytes20] = new TypeInfo[Bytes20] {
    override def name: String = "bytes20"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes20](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes20].value, 20)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes20, Int) = {
      val value = StaticBytes.decode(bytes, 20, position)
      (Bytes20(value), 32)
    }
  }
}
