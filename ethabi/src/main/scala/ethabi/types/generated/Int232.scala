// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int232(val value: BigInt) extends SolType {
  assert(value.bitLength <= 232)
  override def toString = value.toString
}

object Int232 {
  def apply(value: BigInt): Int232 = new Int232(value)
  def from(value: String): Int232 = Int232(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int232] = new TypeInfo[Int232] {
    override def name: String = "int232"
    override def isStatic: Boolean = true
    override def encode[U >: Int232](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int232].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int232, Int) = {
      val value = IntType.decode(bytes, 232, position)
      (Int232(value), 32)
    }
  }
}
