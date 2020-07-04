// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint176(val value: BigInt) extends SolType {
  assert(value.bitLength <= 176)
  override def toString = value.toString
}

object Uint176 {
  def apply(value: BigInt): Uint176 = new Uint176(value)
  def from(value: String): Uint176 = Uint176(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Uint176] = new TypeInfo[Uint176] {
    override def name: String = "uint176"
    override def isStatic: Boolean = true
    override def encode[U >: Uint176](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint176].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint176, Int) = {
      val value = UintType.decode(bytes, 176, position)
      (Uint176(value), 32)
    }
  }
}
