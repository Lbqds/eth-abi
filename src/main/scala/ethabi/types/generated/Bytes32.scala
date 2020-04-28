// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex

final class Bytes32(val value: Array[Byte]) extends SolType {
  assert(value.length <= 32)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}

object Bytes32 {
  def apply(value: Array[Byte]): Bytes32 = new Bytes32(value)
  def from(value: String): Bytes32 = new Bytes32(Hex.hex2Bytes(value))

  implicit lazy val typeInfo: TypeInfo[Bytes32] = new TypeInfo[Bytes32] {
    override def name: String = "bytes32"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes32](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes32].value, 32)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes32, Int) = {
      val value = StaticBytes.decode(bytes, 32, position)
      (Bytes32(value), 32)
    }
  }
}
