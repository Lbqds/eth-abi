// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int72(val value: BigInt) extends SolType {
  assert(value.bitLength <= 72)
  override def toString = value.toString
}
object Int72 {
  def apply(value: BigInt): Int72 = new Int72(value)
  implicit lazy val typeInfo: TypeInfo[Int72] = new TypeInfo[Int72] {
    override def name: String = "int72"
    override def isStatic: Boolean = true
    override def encode[U >: Int72](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int72].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int72, Int) = {
      val value = IntType.decode(bytes, 72, position)
      (Int72(value), 32)
    }
  }
}