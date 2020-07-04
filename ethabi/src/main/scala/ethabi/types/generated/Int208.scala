// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int208(val value: BigInt) extends SolType {
  assert(value.bitLength <= 208)
  override def toString = value.toString
}

object Int208 {
  def apply(value: BigInt): Int208 = new Int208(value)
  def from(value: String): Int208 = Int208(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int208] = new TypeInfo[Int208] {
    override def name: String = "int208"
    override def isStatic: Boolean = true
    override def encode[U >: Int208](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int208].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int208, Int) = {
      val value = IntType.decode(bytes, 208, position)
      (Int208(value), 32)
    }
  }
}
