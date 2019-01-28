// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int104(val value: BigInt) extends SolType {
  assert(value.bitLength <= 104)
  override def toString = value.toString
}
object Int104 {
  def apply(value: BigInt): Int104 = new Int104(value)
  implicit lazy val typeInfo: TypeInfo[Int104] = new TypeInfo[Int104] {
    override def name: String = "int104"
    override def isStatic: Boolean = true
    override def encode[U >: Int104](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int104].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int104, Int) = {
      val value = IntType.decode(bytes, 104, position)
      (Int104(value), 32)
    }
  }
}