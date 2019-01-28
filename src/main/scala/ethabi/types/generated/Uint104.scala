// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint104(val value: BigInt) extends SolType {
  assert(value.bitLength <= 104)
  override def toString = value.toString
}
object Uint104 {
  def apply(value: BigInt): Uint104 = new Uint104(value)
  implicit lazy val typeInfo: TypeInfo[Uint104] = new TypeInfo[Uint104] {
    override def name: String = "uint104"
    override def isStatic: Boolean = true
    override def encode[U >: Uint104](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint104].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint104, Int) = {
      val value = UintType.decode(bytes, 104, position)
      (Uint104(value), 32)
    }
  }
}