// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes8(val value: Array[Byte]) extends SolType {
  assert(value.length <= 8)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes8 {
  def apply(value: Array[Byte]): Bytes8 = new Bytes8(value)
  implicit lazy val typeInfo: TypeInfo[Bytes8] = new TypeInfo[Bytes8] {
    override def name: String = "bytes8"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes8](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes8].value, 8)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes8, Int) = {
      val value = StaticBytes.decode(bytes, 8, position)
      (Bytes8(value), 32)
    }
  }
}