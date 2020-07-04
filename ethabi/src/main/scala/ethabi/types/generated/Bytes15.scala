// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex

final class Bytes15(val value: Array[Byte]) extends SolType {
  assert(value.length <= 15)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}

object Bytes15 {
  def apply(value: Array[Byte]): Bytes15 = new Bytes15(value)
  def from(value: String): Bytes15 = new Bytes15(Hex.hex2Bytes(value))

  implicit lazy val typeInfo: TypeInfo[Bytes15] = new TypeInfo[Bytes15] {
    override def name: String = "bytes15"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes15](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes15].value, 15)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes15, Int) = {
      val value = StaticBytes.decode(bytes, 15, position)
      (Bytes15(value), 32)
    }
  }
}
