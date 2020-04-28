// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int192(val value: BigInt) extends SolType {
  assert(value.bitLength <= 192)
  override def toString = value.toString
}

object Int192 {
  def apply(value: BigInt): Int192 = new Int192(value)
  def from(value: String): Int192 = Int192(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int192] = new TypeInfo[Int192] {
    override def name: String = "int192"
    override def isStatic: Boolean = true
    override def encode[U >: Int192](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int192].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int192, Int) = {
      val value = IntType.decode(bytes, 192, position)
      (Int192(value), 32)
    }
  }
}
