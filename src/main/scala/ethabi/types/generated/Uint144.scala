// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint144(val value: BigInt) extends SolType {
  assert(value.bitLength <= 144)
  override def toString = value.toString
}

object Uint144 {
  def apply(value: BigInt): Uint144 = new Uint144(value)
  def from(value: String): Uint144 = Uint144(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Uint144] = new TypeInfo[Uint144] {
    override def name: String = "uint144"
    override def isStatic: Boolean = true
    override def encode[U >: Uint144](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint144].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint144, Int) = {
      val value = UintType.decode(bytes, 144, position)
      (Uint144(value), 32)
    }
  }
}
