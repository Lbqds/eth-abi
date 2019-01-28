// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes11(val value: Array[Byte]) extends SolType {
  assert(value.length <= 11)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes11 {
  def apply(value: Array[Byte]): Bytes11 = new Bytes11(value)
  implicit lazy val typeInfo: TypeInfo[Bytes11] = new TypeInfo[Bytes11] {
    override def name: String = "bytes11"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes11](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes11].value, 11)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes11, Int) = {
      val value = StaticBytes.decode(bytes, 11, position)
      (Bytes11(value), 32)
    }
  }
}