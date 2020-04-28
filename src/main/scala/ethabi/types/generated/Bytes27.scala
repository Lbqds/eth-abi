// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex

final class Bytes27(val value: Array[Byte]) extends SolType {
  assert(value.length <= 27)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}

object Bytes27 {
  def apply(value: Array[Byte]): Bytes27 = new Bytes27(value)
  def from(value: String): Bytes27 = new Bytes27(Hex.hex2Bytes(value))

  implicit lazy val typeInfo: TypeInfo[Bytes27] = new TypeInfo[Bytes27] {
    override def name: String = "bytes27"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes27](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes27].value, 27)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes27, Int) = {
      val value = StaticBytes.decode(bytes, 27, position)
      (Bytes27(value), 32)
    }
  }
}
