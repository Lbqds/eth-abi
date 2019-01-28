// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int128(val value: BigInt) extends SolType {
  assert(value.bitLength <= 128)
  override def toString = value.toString
}
object Int128 {
  def apply(value: BigInt): Int128 = new Int128(value)
  implicit lazy val typeInfo: TypeInfo[Int128] = new TypeInfo[Int128] {
    override def name: String = "int128"
    override def isStatic: Boolean = true
    override def encode[U >: Int128](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int128].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int128, Int) = {
      val value = IntType.decode(bytes, 128, position)
      (Int128(value), 32)
    }
  }
}