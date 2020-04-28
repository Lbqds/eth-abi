// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint80(val value: BigInt) extends SolType {
  assert(value.bitLength <= 80)
  override def toString = value.toString
}

object Uint80 {
  def apply(value: BigInt): Uint80 = new Uint80(value)
  def from(value: String): Uint80 = Uint80(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Uint80] = new TypeInfo[Uint80] {
    override def name: String = "uint80"
    override def isStatic: Boolean = true
    override def encode[U >: Uint80](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint80].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint80, Int) = {
      val value = UintType.decode(bytes, 80, position)
      (Uint80(value), 32)
    }
  }
}
