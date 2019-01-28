// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint96(val value: BigInt) extends SolType {
  assert(value.bitLength <= 96)
  override def toString = value.toString
}
object Uint96 {
  def apply(value: BigInt): Uint96 = new Uint96(value)
  implicit lazy val typeInfo: TypeInfo[Uint96] = new TypeInfo[Uint96] {
    override def name: String = "uint96"
    override def isStatic: Boolean = true
    override def encode[U >: Uint96](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint96].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint96, Int) = {
      val value = UintType.decode(bytes, 96, position)
      (Uint96(value), 32)
    }
  }
}