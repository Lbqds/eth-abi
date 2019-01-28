// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes18(val value: Array[Byte]) extends SolType {
  assert(value.length <= 18)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes18 {
  def apply(value: Array[Byte]): Bytes18 = new Bytes18(value)
  implicit lazy val typeInfo: TypeInfo[Bytes18] = new TypeInfo[Bytes18] {
    override def name: String = "bytes18"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes18](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes18].value, 18)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes18, Int) = {
      val value = StaticBytes.decode(bytes, 18, position)
      (Bytes18(value), 32)
    }
  }
}