// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint240(val value: BigInt) extends SolType {
  assert(value.bitLength <= 240)
  override def toString = value.toString
}

object Uint240 {
  def apply(value: BigInt): Uint240 = new Uint240(value)
  def from(value: String): Uint240 = Uint240(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Uint240] = new TypeInfo[Uint240] {
    override def name: String = "uint240"
    override def isStatic: Boolean = true
    override def encode[U >: Uint240](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint240].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint240, Int) = {
      val value = UintType.decode(bytes, 240, position)
      (Uint240(value), 32)
    }
  }
}
