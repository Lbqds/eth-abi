// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint168(val value: BigInt) extends SolType {
  assert(value.bitLength <= 168)
  override def toString = value.toString
}
object Uint168 {
  def apply(value: BigInt): Uint168 = new Uint168(value)
  implicit lazy val typeInfo: TypeInfo[Uint168] = new TypeInfo[Uint168] {
    override def name: String = "uint168"
    override def isStatic: Boolean = true
    override def encode[U >: Uint168](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint168].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint168, Int) = {
      val value = UintType.decode(bytes, 168, position)
      (Uint168(value), 32)
    }
  }
}