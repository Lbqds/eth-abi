// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint136(val value: BigInt) extends SolType {
  assert(value.bitLength <= 136)
  override def toString = value.toString
}

object Uint136 {
  def apply(value: BigInt): Uint136 = new Uint136(value)
  def from(value: String): Uint136 = Uint136(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Uint136] = new TypeInfo[Uint136] {
    override def name: String = "uint136"
    override def isStatic: Boolean = true
    override def encode[U >: Uint136](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint136].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint136, Int) = {
      val value = UintType.decode(bytes, 136, position)
      (Uint136(value), 32)
    }
  }
}
