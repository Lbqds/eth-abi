// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int160(val value: BigInt) extends SolType {
  assert(value.bitLength <= 160)
  override def toString = value.toString
}

object Int160 {
  def apply(value: BigInt): Int160 = new Int160(value)
  def from(value: String): Int160 = Int160(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int160] = new TypeInfo[Int160] {
    override def name: String = "int160"
    override def isStatic: Boolean = true
    override def encode[U >: Int160](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int160].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int160, Int) = {
      val value = IntType.decode(bytes, 160, position)
      (Int160(value), 32)
    }
  }
}
