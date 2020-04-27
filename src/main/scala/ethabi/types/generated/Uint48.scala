// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint48(val value: BigInt) extends SolType {
  assert(value.bitLength <= 48)
  override def toString = value.toString
}

object Uint48 {
  def apply(value: BigInt): Uint48 = new Uint48(value)
  def from(value: String): Uint48 = Uint48(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Uint48] = new TypeInfo[Uint48] {
    override def name: String = "uint48"
    override def isStatic: Boolean = true
    override def encode[U >: Uint48](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint48].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint48, Int) = {
      val value = UintType.decode(bytes, 48, position)
      (Uint48(value), 32)
    }
  }
}
