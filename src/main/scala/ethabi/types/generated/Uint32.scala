// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint32(val value: BigInt) extends SolType {
  assert(value.bitLength <= 32)
  override def toString = value.toString
}

object Uint32 {
  def apply(value: BigInt): Uint32 = new Uint32(value)
  def from(value: String): Uint32 = Uint32(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Uint32] = new TypeInfo[Uint32] {
    override def name: String = "uint32"
    override def isStatic: Boolean = true
    override def encode[U >: Uint32](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint32].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint32, Int) = {
      val value = UintType.decode(bytes, 32, position)
      (Uint32(value), 32)
    }
  }
}
