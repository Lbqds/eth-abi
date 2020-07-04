// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int40(val value: BigInt) extends SolType {
  assert(value.bitLength <= 40)
  override def toString = value.toString
}

object Int40 {
  def apply(value: BigInt): Int40 = new Int40(value)
  def from(value: String): Int40 = Int40(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int40] = new TypeInfo[Int40] {
    override def name: String = "int40"
    override def isStatic: Boolean = true
    override def encode[U >: Int40](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int40].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int40, Int) = {
      val value = IntType.decode(bytes, 40, position)
      (Int40(value), 32)
    }
  }
}
