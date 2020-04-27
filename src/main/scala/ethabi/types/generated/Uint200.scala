// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint200(val value: BigInt) extends SolType {
  assert(value.bitLength <= 200)
  override def toString = value.toString
}

object Uint200 {
  def apply(value: BigInt): Uint200 = new Uint200(value)
  def from(value: String): Uint200 = Uint200(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Uint200] = new TypeInfo[Uint200] {
    override def name: String = "uint200"
    override def isStatic: Boolean = true
    override def encode[U >: Uint200](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint200].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint200, Int) = {
      val value = UintType.decode(bytes, 200, position)
      (Uint200(value), 32)
    }
  }
}
