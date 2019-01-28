// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint184(val value: BigInt) extends SolType {
  assert(value.bitLength <= 184)
  override def toString = value.toString
}
object Uint184 {
  def apply(value: BigInt): Uint184 = new Uint184(value)
  implicit lazy val typeInfo: TypeInfo[Uint184] = new TypeInfo[Uint184] {
    override def name: String = "uint184"
    override def isStatic: Boolean = true
    override def encode[U >: Uint184](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint184].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint184, Int) = {
      val value = UintType.decode(bytes, 184, position)
      (Uint184(value), 32)
    }
  }
}