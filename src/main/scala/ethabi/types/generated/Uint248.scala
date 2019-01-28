// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint248(val value: BigInt) extends SolType {
  assert(value.bitLength <= 248)
  override def toString = value.toString
}
object Uint248 {
  def apply(value: BigInt): Uint248 = new Uint248(value)
  implicit lazy val typeInfo: TypeInfo[Uint248] = new TypeInfo[Uint248] {
    override def name: String = "uint248"
    override def isStatic: Boolean = true
    override def encode[U >: Uint248](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint248].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint248, Int) = {
      val value = UintType.decode(bytes, 248, position)
      (Uint248(value), 32)
    }
  }
}