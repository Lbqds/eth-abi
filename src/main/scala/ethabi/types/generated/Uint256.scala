// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint256(val value: BigInt) extends SolType {
  assert(value.bitLength <= 256)
  override def toString = value.toString
}
object Uint256 {
  def apply(value: BigInt): Uint256 = new Uint256(value)
  implicit lazy val typeInfo: TypeInfo[Uint256] = new TypeInfo[Uint256] {
    override def name: String = "uint256"
    override def isStatic: Boolean = true
    override def encode[U >: Uint256](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint256].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint256, Int) = {
      val value = UintType.decode(bytes, 256, position)
      (Uint256(value), 32)
    }
  }
}