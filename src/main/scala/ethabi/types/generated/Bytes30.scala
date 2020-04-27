// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex

final class Bytes30(val value: Array[Byte]) extends SolType {
  assert(value.length <= 30)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}

object Bytes30 {
  def apply(value: Array[Byte]): Bytes30 = new Bytes30(value)
  def from(value: String): Bytes30 = new Bytes30(Hex.hex2Bytes(value))

  implicit lazy val typeInfo: TypeInfo[Bytes30] = new TypeInfo[Bytes30] {
    override def name: String = "bytes30"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes30](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes30].value, 30)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes30, Int) = {
      val value = StaticBytes.decode(bytes, 30, position)
      (Bytes30(value), 32)
    }
  }
}
