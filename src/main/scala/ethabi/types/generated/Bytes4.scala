// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes4(val value: Array[Byte]) extends SolType {
  assert(value.length <= 4)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes4 {
  def apply(value: Array[Byte]): Bytes4 = new Bytes4(value)
  implicit lazy val typeInfo: TypeInfo[Bytes4] = new TypeInfo[Bytes4] {
    override def name: String = "bytes4"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes4](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes4].value, 4)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes4, Int) = {
      val value = StaticBytes.decode(bytes, 4, position)
      (Bytes4(value), 32)
    }
  }
}