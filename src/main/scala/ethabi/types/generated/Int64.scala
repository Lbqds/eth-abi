// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int64(val value: BigInt) extends SolType {
  assert(value.bitLength <= 64)
  override def toString = value.toString
}

object Int64 {
  def apply(value: BigInt): Int64 = new Int64(value)
  def from(value: String): Int64 = Int64(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int64] = new TypeInfo[Int64] {
    override def name: String = "int64"
    override def isStatic: Boolean = true
    override def encode[U >: Int64](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int64].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int64, Int) = {
      val value = IntType.decode(bytes, 64, position)
      (Int64(value), 32)
    }
  }
}
