// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes17(val value: Array[Byte]) extends SolType {
  assert(value.length <= 17)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes17 {
  def apply(value: Array[Byte]): Bytes17 = new Bytes17(value)
  implicit lazy val typeInfo: TypeInfo[Bytes17] = new TypeInfo[Bytes17] {
    override def name: String = "bytes17"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes17](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes17].value, 17)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes17, Int) = {
      val value = StaticBytes.decode(bytes, 17, position)
      (Bytes17(value), 32)
    }
  }
}