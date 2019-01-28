// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint72(val value: BigInt) extends SolType {
  assert(value.bitLength <= 72)
  override def toString = value.toString
}
object Uint72 {
  def apply(value: BigInt): Uint72 = new Uint72(value)
  implicit lazy val typeInfo: TypeInfo[Uint72] = new TypeInfo[Uint72] {
    override def name: String = "uint72"
    override def isStatic: Boolean = true
    override def encode[U >: Uint72](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint72].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint72, Int) = {
      val value = UintType.decode(bytes, 72, position)
      (Uint72(value), 32)
    }
  }
}