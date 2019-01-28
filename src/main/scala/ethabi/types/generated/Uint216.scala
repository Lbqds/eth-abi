// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint216(val value: BigInt) extends SolType {
  assert(value.bitLength <= 216)
  override def toString = value.toString
}
object Uint216 {
  def apply(value: BigInt): Uint216 = new Uint216(value)
  implicit lazy val typeInfo: TypeInfo[Uint216] = new TypeInfo[Uint216] {
    override def name: String = "uint216"
    override def isStatic: Boolean = true
    override def encode[U >: Uint216](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint216].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint216, Int) = {
      val value = UintType.decode(bytes, 216, position)
      (Uint216(value), 32)
    }
  }
}