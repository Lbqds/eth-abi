// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex

final class Bytes16(val value: Array[Byte]) extends SolType {
  assert(value.length <= 16)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}

object Bytes16 {
  def apply(value: Array[Byte]): Bytes16 = new Bytes16(value)
  def from(value: String): Bytes16 = new Bytes16(Hex.hex2Bytes(value))

  implicit lazy val typeInfo: TypeInfo[Bytes16] = new TypeInfo[Bytes16] {
    override def name: String = "bytes16"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes16](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes16].value, 16)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes16, Int) = {
      val value = StaticBytes.decode(bytes, 16, position)
      (Bytes16(value), 32)
    }
  }
}
