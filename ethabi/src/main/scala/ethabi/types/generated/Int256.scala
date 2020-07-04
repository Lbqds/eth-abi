// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int256(val value: BigInt) extends SolType {
  assert(value.bitLength <= 256)
  override def toString = value.toString
}

object Int256 {
  def apply(value: BigInt): Int256 = new Int256(value)
  def from(value: String): Int256 = Int256(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int256] = new TypeInfo[Int256] {
    override def name: String = "int256"
    override def isStatic: Boolean = true
    override def encode[U >: Int256](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int256].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int256, Int) = {
      val value = IntType.decode(bytes, 256, position)
      (Int256(value), 32)
    }
  }
}
