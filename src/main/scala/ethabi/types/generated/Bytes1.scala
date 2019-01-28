// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes1(val value: Array[Byte]) extends SolType {
  assert(value.length <= 1)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes1 {
  def apply(value: Array[Byte]): Bytes1 = new Bytes1(value)
  implicit lazy val typeInfo: TypeInfo[Bytes1] = new TypeInfo[Bytes1] {
    override def name: String = "bytes1"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes1](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes1].value, 1)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes1, Int) = {
      val value = StaticBytes.decode(bytes, 1, position)
      (Bytes1(value), 32)
    }
  }
}