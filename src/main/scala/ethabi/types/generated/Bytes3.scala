// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex

final class Bytes3(val value: Array[Byte]) extends SolType {
  assert(value.length <= 3)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}

object Bytes3 {
  def apply(value: Array[Byte]): Bytes3 = new Bytes3(value)
  def from(value: String): Bytes3 = new Bytes3(Hex.hex2Bytes(value))

  implicit lazy val typeInfo: TypeInfo[Bytes3] = new TypeInfo[Bytes3] {
    override def name: String = "bytes3"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes3](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes3].value, 3)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes3, Int) = {
      val value = StaticBytes.decode(bytes, 3, position)
      (Bytes3(value), 32)
    }
  }
}
