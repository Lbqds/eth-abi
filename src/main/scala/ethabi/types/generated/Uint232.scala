// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint232(val value: BigInt) extends SolType {
  assert(value.bitLength <= 232)
  override def toString = value.toString
}
object Uint232 {
  def apply(value: BigInt): Uint232 = new Uint232(value)
  implicit lazy val typeInfo: TypeInfo[Uint232] = new TypeInfo[Uint232] {
    override def name: String = "uint232"
    override def isStatic: Boolean = true
    override def encode[U >: Uint232](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint232].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint232, Int) = {
      val value = UintType.decode(bytes, 232, position)
      (Uint232(value), 32)
    }
  }
}