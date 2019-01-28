// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint24(val value: BigInt) extends SolType {
  assert(value.bitLength <= 24)
  override def toString = value.toString
}
object Uint24 {
  def apply(value: BigInt): Uint24 = new Uint24(value)
  implicit lazy val typeInfo: TypeInfo[Uint24] = new TypeInfo[Uint24] {
    override def name: String = "uint24"
    override def isStatic: Boolean = true
    override def encode[U >: Uint24](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint24].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint24, Int) = {
      val value = UintType.decode(bytes, 24, position)
      (Uint24(value), 32)
    }
  }
}