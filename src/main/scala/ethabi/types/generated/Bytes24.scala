// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes24(val value: Array[Byte]) extends SolType {
  assert(value.length <= 24)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes24 {
  def apply(value: Array[Byte]): Bytes24 = new Bytes24(value)
  implicit lazy val typeInfo: TypeInfo[Bytes24] = new TypeInfo[Bytes24] {
    override def name: String = "bytes24"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes24](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes24].value, 24)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes24, Int) = {
      val value = StaticBytes.decode(bytes, 24, position)
      (Bytes24(value), 32)
    }
  }
}