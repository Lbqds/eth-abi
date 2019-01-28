// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint208(val value: BigInt) extends SolType {
  assert(value.bitLength <= 208)
  override def toString = value.toString
}
object Uint208 {
  def apply(value: BigInt): Uint208 = new Uint208(value)
  implicit lazy val typeInfo: TypeInfo[Uint208] = new TypeInfo[Uint208] {
    override def name: String = "uint208"
    override def isStatic: Boolean = true
    override def encode[U >: Uint208](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint208].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint208, Int) = {
      val value = UintType.decode(bytes, 208, position)
      (Uint208(value), 32)
    }
  }
}