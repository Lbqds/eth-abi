// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int240(val value: BigInt) extends SolType {
  assert(value.bitLength <= 240)
  override def toString = value.toString
}

object Int240 {
  def apply(value: BigInt): Int240 = new Int240(value)
  def from(value: String): Int240 = Int240(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int240] = new TypeInfo[Int240] {
    override def name: String = "int240"
    override def isStatic: Boolean = true
    override def encode[U >: Int240](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int240].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int240, Int) = {
      val value = IntType.decode(bytes, 240, position)
      (Int240(value), 32)
    }
  }
}
