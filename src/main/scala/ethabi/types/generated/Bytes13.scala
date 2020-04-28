// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex

final class Bytes13(val value: Array[Byte]) extends SolType {
  assert(value.length <= 13)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}

object Bytes13 {
  def apply(value: Array[Byte]): Bytes13 = new Bytes13(value)
  def from(value: String): Bytes13 = new Bytes13(Hex.hex2Bytes(value))

  implicit lazy val typeInfo: TypeInfo[Bytes13] = new TypeInfo[Bytes13] {
    override def name: String = "bytes13"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes13](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes13].value, 13)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes13, Int) = {
      val value = StaticBytes.decode(bytes, 13, position)
      (Bytes13(value), 32)
    }
  }
}
