// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated

final class Int224(val value: BigInt) extends SolType {
  assert(value.bitLength <= 224)
  override def toString = value.toString
}

object Int224 {
  def apply(value: BigInt): Int224 = new Int224(value)
  def from(value: String): Int224 = Int224(BigInt(value))

  implicit lazy val typeInfo: TypeInfo[Int224] = new TypeInfo[Int224] {
    override def name: String = "int224"
    override def isStatic: Boolean = true
    override def encode[U >: Int224](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int224].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int224, Int) = {
      val value = IntType.decode(bytes, 224, position)
      (Int224(value), 32)
    }
  }
}
