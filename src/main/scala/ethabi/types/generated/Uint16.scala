// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint16(val value: BigInt) extends SolType {
  assert(value.bitLength <= 16)
  override def toString = value.toString
}

object Uint16 {
  def apply(value: BigInt): Uint16 = new Uint16(value)
  def from(value: String): Uint16 = Uint16(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Uint16] = new TypeInfo[Uint16] {
    override def name: String = "uint16"
    override def isStatic: Boolean = true
    override def encode[U >: Uint16](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint16].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint16, Int) = {
      val value = UintType.decode(bytes, 16, position)
      (Uint16(value), 32)
    }
  }
}
