// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes2(val value: Array[Byte]) extends SolType {
  assert(value.length <= 2)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes2 {
  def apply(value: Array[Byte]): Bytes2 = new Bytes2(value)
  implicit lazy val typeInfo: TypeInfo[Bytes2] = new TypeInfo[Bytes2] {
    override def name: String = "bytes2"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes2](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes2].value, 2)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes2, Int) = {
      val value = StaticBytes.decode(bytes, 2, position)
      (Bytes2(value), 32)
    }
  }
}