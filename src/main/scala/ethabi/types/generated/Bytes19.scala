// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex

final class Bytes19(val value: Array[Byte]) extends SolType {
  assert(value.length <= 19)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}

object Bytes19 {
  def apply(value: Array[Byte]): Bytes19 = new Bytes19(value)
  def from(value: String): Bytes19 = new Bytes19(Hex.hex2Bytes(value))

  implicit lazy val typeInfo: TypeInfo[Bytes19] = new TypeInfo[Bytes19] {
    override def name: String = "bytes19"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes19](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes19].value, 19)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes19, Int) = {
      val value = StaticBytes.decode(bytes, 19, position)
      (Bytes19(value), 32)
    }
  }
}
