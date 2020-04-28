// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int56(val value: BigInt) extends SolType {
  assert(value.bitLength <= 56)
  override def toString = value.toString
}

object Int56 {
  def apply(value: BigInt): Int56 = new Int56(value)
  def from(value: String): Int56 = Int56(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int56] = new TypeInfo[Int56] {
    override def name: String = "int56"
    override def isStatic: Boolean = true
    override def encode[U >: Int56](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int56].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int56, Int) = {
      val value = IntType.decode(bytes, 56, position)
      (Int56(value), 32)
    }
  }
}
