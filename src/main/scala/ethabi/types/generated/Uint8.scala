// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint8(val value: BigInt) extends SolType {
  assert(value.bitLength <= 8)
  override def toString = value.toString
}
object Uint8 {
  def apply(value: BigInt): Uint8 = new Uint8(value)
  implicit lazy val typeInfo: TypeInfo[Uint8] = new TypeInfo[Uint8] {
    override def name: String = "uint8"
    override def isStatic: Boolean = true
    override def encode[U >: Uint8](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint8].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint8, Int) = {
      val value = UintType.decode(bytes, 8, position)
      (Uint8(value), 32)
    }
  }
}