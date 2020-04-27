// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex

final class Bytes23(val value: Array[Byte]) extends SolType {
  assert(value.length <= 23)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}

object Bytes23 {
  def apply(value: Array[Byte]): Bytes23 = new Bytes23(value)
  def from(value: String): Bytes23 = new Bytes23(Hex.hex2Bytes(value))

  implicit lazy val typeInfo: TypeInfo[Bytes23] = new TypeInfo[Bytes23] {
    override def name: String = "bytes23"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes23](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes23].value, 23)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes23, Int) = {
      val value = StaticBytes.decode(bytes, 23, position)
      (Bytes23(value), 32)
    }
  }
}
