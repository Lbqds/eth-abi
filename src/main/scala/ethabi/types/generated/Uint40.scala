// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint40(val value: BigInt) extends SolType {
  assert(value.bitLength <= 40)
  override def toString = value.toString
}
object Uint40 {
  def apply(value: BigInt): Uint40 = new Uint40(value)
  implicit lazy val typeInfo: TypeInfo[Uint40] = new TypeInfo[Uint40] {
    override def name: String = "uint40"
    override def isStatic: Boolean = true
    override def encode[U >: Uint40](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint40].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint40, Int) = {
      val value = UintType.decode(bytes, 40, position)
      (Uint40(value), 32)
    }
  }
}