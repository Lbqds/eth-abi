// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint64(val value: BigInt) extends SolType {
  assert(value.bitLength <= 64)
  override def toString = value.toString
}
object Uint64 {
  def apply(value: BigInt): Uint64 = new Uint64(value)
  implicit lazy val typeInfo: TypeInfo[Uint64] = new TypeInfo[Uint64] {
    override def name: String = "uint64"
    override def isStatic: Boolean = true
    override def encode[U >: Uint64](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint64].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint64, Int) = {
      val value = UintType.decode(bytes, 64, position)
      (Uint64(value), 32)
    }
  }
}