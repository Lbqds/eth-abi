// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int136(val value: BigInt) extends SolType {
  assert(value.bitLength <= 136)
  override def toString = value.toString
}
object Int136 {
  def apply(value: BigInt): Int136 = new Int136(value)
  implicit lazy val typeInfo: TypeInfo[Int136] = new TypeInfo[Int136] {
    override def name: String = "int136"
    override def isStatic: Boolean = true
    override def encode[U >: Int136](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int136].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int136, Int) = {
      val value = IntType.decode(bytes, 136, position)
      (Int136(value), 32)
    }
  }
}