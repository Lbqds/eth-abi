// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int168(val value: BigInt) extends SolType {
  assert(value.bitLength <= 168)
  override def toString = value.toString
}

object Int168 {
  def apply(value: BigInt): Int168 = new Int168(value)
  def from(value: String): Int168 = Int168(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int168] = new TypeInfo[Int168] {
    override def name: String = "int168"
    override def isStatic: Boolean = true
    override def encode[U >: Int168](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int168].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int168, Int) = {
      val value = IntType.decode(bytes, 168, position)
      (Int168(value), 32)
    }
  }
}
