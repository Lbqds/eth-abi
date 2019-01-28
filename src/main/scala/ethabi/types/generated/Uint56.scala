// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint56(val value: BigInt) extends SolType {
  assert(value.bitLength <= 56)
  override def toString = value.toString
}
object Uint56 {
  def apply(value: BigInt): Uint56 = new Uint56(value)
  implicit lazy val typeInfo: TypeInfo[Uint56] = new TypeInfo[Uint56] {
    override def name: String = "uint56"
    override def isStatic: Boolean = true
    override def encode[U >: Uint56](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint56].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint56, Int) = {
      val value = UintType.decode(bytes, 56, position)
      (Uint56(value), 32)
    }
  }
}