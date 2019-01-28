// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint88(val value: BigInt) extends SolType {
  assert(value.bitLength <= 88)
  override def toString = value.toString
}
object Uint88 {
  def apply(value: BigInt): Uint88 = new Uint88(value)
  implicit lazy val typeInfo: TypeInfo[Uint88] = new TypeInfo[Uint88] {
    override def name: String = "uint88"
    override def isStatic: Boolean = true
    override def encode[U >: Uint88](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint88].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint88, Int) = {
      val value = UintType.decode(bytes, 88, position)
      (Uint88(value), 32)
    }
  }
}