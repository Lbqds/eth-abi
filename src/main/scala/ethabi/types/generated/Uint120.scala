// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint120(val value: BigInt) extends SolType {
  assert(value.bitLength <= 120)
  override def toString = value.toString
}
object Uint120 {
  def apply(value: BigInt): Uint120 = new Uint120(value)
  implicit lazy val typeInfo: TypeInfo[Uint120] = new TypeInfo[Uint120] {
    override def name: String = "uint120"
    override def isStatic: Boolean = true
    override def encode[U >: Uint120](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint120].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint120, Int) = {
      val value = UintType.decode(bytes, 120, position)
      (Uint120(value), 32)
    }
  }
}