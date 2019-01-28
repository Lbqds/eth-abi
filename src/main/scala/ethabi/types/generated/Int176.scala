// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int176(val value: BigInt) extends SolType {
  assert(value.bitLength <= 176)
  override def toString = value.toString
}
object Int176 {
  def apply(value: BigInt): Int176 = new Int176(value)
  implicit lazy val typeInfo: TypeInfo[Int176] = new TypeInfo[Int176] {
    override def name: String = "int176"
    override def isStatic: Boolean = true
    override def encode[U >: Int176](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int176].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int176, Int) = {
      val value = IntType.decode(bytes, 176, position)
      (Int176(value), 32)
    }
  }
}