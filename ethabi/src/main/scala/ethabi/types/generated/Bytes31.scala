// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex

final class Bytes31(val value: Array[Byte]) extends SolType {
  assert(value.length <= 31)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}

object Bytes31 {
  def apply(value: Array[Byte]): Bytes31 = new Bytes31(value)
  def from(value: String): Bytes31 = new Bytes31(Hex.hex2Bytes(value))

  implicit lazy val typeInfo: TypeInfo[Bytes31] = new TypeInfo[Bytes31] {
    override def name: String = "bytes31"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes31](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes31].value, 31)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes31, Int) = {
      val value = StaticBytes.decode(bytes, 31, position)
      (Bytes31(value), 32)
    }
  }
}
