// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint160(val value: BigInt) extends SolType {
  assert(value.bitLength <= 160)
  override def toString = value.toString
}

object Uint160 {
  def apply(value: BigInt): Uint160 = new Uint160(value)
  def from(value: String): Uint160 = Uint160(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Uint160] = new TypeInfo[Uint160] {
    override def name: String = "uint160"
    override def isStatic: Boolean = true
    override def encode[U >: Uint160](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint160].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint160, Int) = {
      val value = UintType.decode(bytes, 160, position)
      (Uint160(value), 32)
    }
  }
}
