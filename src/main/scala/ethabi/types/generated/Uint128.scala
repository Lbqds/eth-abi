// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint128(val value: BigInt) extends SolType {
  assert(value.bitLength <= 128)
  override def toString = value.toString
}

object Uint128 {
  def apply(value: BigInt): Uint128 = new Uint128(value)
  def from(value: String): Uint128 = Uint128(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Uint128] = new TypeInfo[Uint128] {
    override def name: String = "uint128"
    override def isStatic: Boolean = true
    override def encode[U >: Uint128](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint128].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint128, Int) = {
      val value = UintType.decode(bytes, 128, position)
      (Uint128(value), 32)
    }
  }
}
