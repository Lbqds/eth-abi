// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
import util.Hex

final class Bytes6(val value: Array[Byte]) extends SolType {
  assert(value.length <= 6)
  override def toString = Hex.bytes2Hex(value, withPrefix = true)
}

object Bytes6 {
  def apply(value: Array[Byte]): Bytes6 = new Bytes6(value)
  def from(value: String): Bytes6 = new Bytes6(Hex.hex2Bytes(value))

  implicit lazy val typeInfo: TypeInfo[Bytes6] = new TypeInfo[Bytes6] {
    override def name: String = "bytes6"
    override def isStatic: Boolean = true
    override def encode[U >: Bytes6](value: U): Array[Byte] = {
      StaticBytes.encode(value.asInstanceOf[Bytes6].value, 6)
    }
    override def decode(bytes: Array[Byte], position: Int): (Bytes6, Int) = {
      val value = StaticBytes.decode(bytes, 6, position)
      (Bytes6(value), 32)
    }
  }
}
