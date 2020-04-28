// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int48(val value: BigInt) extends SolType {
  assert(value.bitLength <= 48)
  override def toString = value.toString
}

object Int48 {
  def apply(value: BigInt): Int48 = new Int48(value)
  def from(value: String): Int48 = Int48(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int48] = new TypeInfo[Int48] {
    override def name: String = "int48"
    override def isStatic: Boolean = true
    override def encode[U >: Int48](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int48].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int48, Int) = {
      val value = IntType.decode(bytes, 48, position)
      (Int48(value), 32)
    }
  }
}
