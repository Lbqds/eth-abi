// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int120(val value: BigInt) extends SolType {
  assert(value.bitLength <= 120)
  override def toString = value.toString
}

object Int120 {
  def apply(value: BigInt): Int120 = new Int120(value)
  def from(value: String): Int120 = Int120(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int120] = new TypeInfo[Int120] {
    override def name: String = "int120"
    override def isStatic: Boolean = true
    override def encode[U >: Int120](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int120].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int120, Int) = {
      val value = IntType.decode(bytes, 120, position)
      (Int120(value), 32)
    }
  }
}
