// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes5(val value: Array[Byte]) extends SolType {
  assert(value.length <= 5)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes5 {
  def apply(value: Array[Byte]): Bytes5 = new Bytes5(value)
  implicit lazy val typeInfo: TypeInfo[Bytes5] = new TypeInfo[Bytes5] {
    override def name: String = "bytes5"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes5](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes5].value, 5)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes5, Int) = {
      val value = StaticBytes.decode(bytes, 5, position)
      (Bytes5(value), 32)
    }
  }
}