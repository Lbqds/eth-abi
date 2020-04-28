// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex

final class Bytes29(val value: Array[Byte]) extends SolType {
  assert(value.length <= 29)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}

object Bytes29 {
  def apply(value: Array[Byte]): Bytes29 = new Bytes29(value)
  def from(value: String): Bytes29 = new Bytes29(Hex.hex2Bytes(value))

  implicit lazy val typeInfo: TypeInfo[Bytes29] = new TypeInfo[Bytes29] {
    override def name: String = "bytes29"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes29](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes29].value, 29)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes29, Int) = {
      val value = StaticBytes.decode(bytes, 29, position)
      (Bytes29(value), 32)
    }
  }
}
