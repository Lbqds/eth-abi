// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint224(val value: BigInt) extends SolType {
  assert(value.bitLength <= 224)
  override def toString = value.toString
}

object Uint224 {
  def apply(value: BigInt): Uint224 = new Uint224(value)
  def from(value: String): Uint224 = Uint224(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Uint224] = new TypeInfo[Uint224] {
    override def name: String = "uint224"
    override def isStatic: Boolean = true
    override def encode[U >: Uint224](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint224].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint224, Int) = {
      val value = UintType.decode(bytes, 224, position)
      (Uint224(value), 32)
    }
  }
}
