// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes28(val value: Array[Byte]) extends SolType {
  assert(value.length <= 28)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes28 {
  def apply(value: Array[Byte]): Bytes28 = new Bytes28(value)
  implicit lazy val typeInfo: TypeInfo[Bytes28] = new TypeInfo[Bytes28] {
    override def name: String = "bytes28"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes28](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes28].value, 28)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes28, Int) = {
      val value = StaticBytes.decode(bytes, 28, position)
      (Bytes28(value), 32)
    }
  }
}