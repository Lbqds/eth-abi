// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int184(val value: BigInt) extends SolType {
  assert(value.bitLength <= 184)
  override def toString = value.toString
}

object Int184 {
  def apply(value: BigInt): Int184 = new Int184(value)
  def from(value: String): Int184 = Int184(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int184] = new TypeInfo[Int184] {
    override def name: String = "int184"
    override def isStatic: Boolean = true
    override def encode[U >: Int184](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int184].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int184, Int) = {
      val value = IntType.decode(bytes, 184, position)
      (Int184(value), 32)
    }
  }
}
