// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes22(val value: Array[Byte]) extends SolType {
  assert(value.length <= 22)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes22 {
  def apply(value: Array[Byte]): Bytes22 = new Bytes22(value)
  implicit lazy val typeInfo: TypeInfo[Bytes22] = new TypeInfo[Bytes22] {
    override def name: String = "bytes22"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes22](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes22].value, 22)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes22, Int) = {
      val value = StaticBytes.decode(bytes, 22, position)
      (Bytes22(value), 32)
    }
  }
}