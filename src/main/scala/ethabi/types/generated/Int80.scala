// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int80(val value: BigInt) extends SolType {
  assert(value.bitLength <= 80)
  override def toString = value.toString
}
object Int80 {
  def apply(value: BigInt): Int80 = new Int80(value)
  implicit lazy val typeInfo: TypeInfo[Int80] = new TypeInfo[Int80] {
    override def name: String = "int80"
    override def isStatic: Boolean = true
    override def encode[U >: Int80](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int80].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int80, Int) = {
      val value = IntType.decode(bytes, 80, position)
      (Int80(value), 32)
    }
  }
}