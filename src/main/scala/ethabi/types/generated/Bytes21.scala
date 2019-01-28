// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes21(val value: Array[Byte]) extends SolType {
  assert(value.length <= 21)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes21 {
  def apply(value: Array[Byte]): Bytes21 = new Bytes21(value)
  implicit lazy val typeInfo: TypeInfo[Bytes21] = new TypeInfo[Bytes21] {
    override def name: String = "bytes21"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes21](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes21].value, 21)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes21, Int) = {
      val value = StaticBytes.decode(bytes, 21, position)
      (Bytes21(value), 32)
    }
  }
}