// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex

final class Bytes26(val value: Array[Byte]) extends SolType {
  assert(value.length <= 26)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}

object Bytes26 {
  def apply(value: Array[Byte]): Bytes26 = new Bytes26(value)
  def from(value: String): Bytes26 = new Bytes26(Hex.hex2Bytes(value))

  implicit lazy val typeInfo: TypeInfo[Bytes26] = new TypeInfo[Bytes26] {
    override def name: String = "bytes26"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes26](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes26].value, 26)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes26, Int) = {
      val value = StaticBytes.decode(bytes, 26, position)
      (Bytes26(value), 32)
    }
  }
}
