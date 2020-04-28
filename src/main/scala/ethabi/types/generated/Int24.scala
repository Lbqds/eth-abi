// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int24(val value: BigInt) extends SolType {
  assert(value.bitLength <= 24)
  override def toString = value.toString
}

object Int24 {
  def apply(value: BigInt): Int24 = new Int24(value)
  def from(value: String): Int24 = Int24(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int24] = new TypeInfo[Int24] {
    override def name: String = "int24"
    override def isStatic: Boolean = true
    override def encode[U >: Int24](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int24].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int24, Int) = {
      val value = IntType.decode(bytes, 24, position)
      (Int24(value), 32)
    }
  }
}
