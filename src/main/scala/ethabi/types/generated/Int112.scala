// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int112(val value: BigInt) extends SolType {
  assert(value.bitLength <= 112)
  override def toString = value.toString
}
object Int112 {
  def apply(value: BigInt): Int112 = new Int112(value)
  implicit lazy val typeInfo: TypeInfo[Int112] = new TypeInfo[Int112] {
    override def name: String = "int112"
    override def isStatic: Boolean = true
    override def encode[U >: Int112](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int112].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int112, Int) = {
      val value = IntType.decode(bytes, 112, position)
      (Int112(value), 32)
    }
  }
}