// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint192(val value: BigInt) extends SolType {
  assert(value.bitLength <= 192)
  override def toString = value.toString
}

object Uint192 {
  def apply(value: BigInt): Uint192 = new Uint192(value)
  def from(value: String): Uint192 = Uint192(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Uint192] = new TypeInfo[Uint192] {
    override def name: String = "uint192"
    override def isStatic: Boolean = true
    override def encode[U >: Uint192](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint192].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint192, Int) = {
      val value = UintType.decode(bytes, 192, position)
      (Uint192(value), 32)
    }
  }
}
