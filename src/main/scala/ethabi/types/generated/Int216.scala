// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int216(val value: BigInt) extends SolType {
  assert(value.bitLength <= 216)
  override def toString = value.toString
}
object Int216 {
  def apply(value: BigInt): Int216 = new Int216(value)
  implicit lazy val typeInfo: TypeInfo[Int216] = new TypeInfo[Int216] {
    override def name: String = "int216"
    override def isStatic: Boolean = true
    override def encode[U >: Int216](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int216].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int216, Int) = {
      val value = IntType.decode(bytes, 216, position)
      (Int216(value), 32)
    }
  }
}