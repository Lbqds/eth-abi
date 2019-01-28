// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes9(val value: Array[Byte]) extends SolType {
  assert(value.length <= 9)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes9 {
  def apply(value: Array[Byte]): Bytes9 = new Bytes9(value)
  implicit lazy val typeInfo: TypeInfo[Bytes9] = new TypeInfo[Bytes9] {
    override def name: String = "bytes9"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes9](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes9].value, 9)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes9, Int) = {
      val value = StaticBytes.decode(bytes, 9, position)
      (Bytes9(value), 32)
    }
  }
}