// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int8(val value: BigInt) extends SolType {
  assert(value.bitLength <= 8)
  override def toString = value.toString
}
object Int8 {
  def apply(value: BigInt): Int8 = new Int8(value)
  implicit lazy val typeInfo: TypeInfo[Int8] = new TypeInfo[Int8] {
    override def name: String = "int8"
    override def isStatic: Boolean = true
    override def encode[U >: Int8](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int8].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int8, Int) = {
      val value = IntType.decode(bytes, 8, position)
      (Int8(value), 32)
    }
  }
}