// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes25(val value: Array[Byte]) extends SolType {
  assert(value.length <= 25)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes25 {
  def apply(value: Array[Byte]): Bytes25 = new Bytes25(value)
  implicit lazy val typeInfo: TypeInfo[Bytes25] = new TypeInfo[Bytes25] {
    override def name: String = "bytes25"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes25](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes25].value, 25)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes25, Int) = {
      val value = StaticBytes.decode(bytes, 25, position)
      (Bytes25(value), 32)
    }
  }
}