// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex
final class Bytes12(val value: Array[Byte]) extends SolType {
  assert(value.length <= 12)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}
object Bytes12 {
  def apply(value: Array[Byte]): Bytes12 = new Bytes12(value)
  implicit lazy val typeInfo: TypeInfo[Bytes12] = new TypeInfo[Bytes12] {
    override def name: String = "bytes12"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes12](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes12].value, 12)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes12, Int) = {
      val value = StaticBytes.decode(bytes, 12, position)
      (Bytes12(value), 32)
    }
  }
}