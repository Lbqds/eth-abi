// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint112(val value: BigInt) extends SolType {
  assert(value.bitLength <= 112)
  override def toString = value.toString
}
object Uint112 {
  def apply(value: BigInt): Uint112 = new Uint112(value)
  implicit lazy val typeInfo: TypeInfo[Uint112] = new TypeInfo[Uint112] {
    override def name: String = "uint112"
    override def isStatic: Boolean = true
    override def encode[U >: Uint112](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint112].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint112, Int) = {
      val value = UintType.decode(bytes, 112, position)
      (Uint112(value), 32)
    }
  }
}