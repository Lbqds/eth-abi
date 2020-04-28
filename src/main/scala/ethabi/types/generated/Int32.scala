// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int32(val value: BigInt) extends SolType {
  assert(value.bitLength <= 32)
  override def toString = value.toString
}

object Int32 {
  def apply(value: BigInt): Int32 = new Int32(value)
  def from(value: String): Int32 = Int32(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int32] = new TypeInfo[Int32] {
    override def name: String = "int32"
    override def isStatic: Boolean = true
    override def encode[U >: Int32](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int32].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int32, Int) = {
      val value = IntType.decode(bytes, 32, position)
      (Int32(value), 32)
    }
  }
}
