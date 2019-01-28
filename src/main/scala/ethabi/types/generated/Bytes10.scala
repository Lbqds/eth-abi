// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes10(val value: Array[Byte]) extends SolType {
  assert(value.length <= 10)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes10 {
  def apply(value: Array[Byte]): Bytes10 = new Bytes10(value)
  implicit lazy val typeInfo: TypeInfo[Bytes10] = new TypeInfo[Bytes10] {
    override def name: String = "bytes10"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes10](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes10].value, 10)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes10, Int) = {
      val value = StaticBytes.decode(bytes, 10, position)
      (Bytes10(value), 32)
    }
  }
}