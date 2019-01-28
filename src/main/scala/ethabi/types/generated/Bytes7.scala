// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes7(val value: Array[Byte]) extends SolType {
  assert(value.length <= 7)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes7 {
  def apply(value: Array[Byte]): Bytes7 = new Bytes7(value)
  implicit lazy val typeInfo: TypeInfo[Bytes7] = new TypeInfo[Bytes7] {
    override def name: String = "bytes7"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes7](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes7].value, 7)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes7, Int) = {
      val value = StaticBytes.decode(bytes, 7, position)
      (Bytes7(value), 32)
    }
  }
}