// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex

final class Bytes14(val value: Array[Byte]) extends SolType {
  assert(value.length <= 14)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}

object Bytes14 {
  def apply(value: Array[Byte]): Bytes14 = new Bytes14(value)
  def from(value: String): Bytes14 = new Bytes14(Hex.hex2Bytes(value))

  implicit lazy val typeInfo: TypeInfo[Bytes14] = new TypeInfo[Bytes14] {
    override def name: String = "bytes14"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes14](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes14].value, 14)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes14, Int) = {
      val value = StaticBytes.decode(bytes, 14, position)
      (Bytes14(value), 32)
    }
  }
}
