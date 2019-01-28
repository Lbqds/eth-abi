// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int16(val value: BigInt) extends SolType {
  assert(value.bitLength <= 16)
  override def toString = value.toString
}
object Int16 {
  def apply(value: BigInt): Int16 = new Int16(value)
  implicit lazy val typeInfo: TypeInfo[Int16] = new TypeInfo[Int16] {
    override def name: String = "int16"
    override def isStatic: Boolean = true
    override def encode[U >: Int16](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int16].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int16, Int) = {
      val value = IntType.decode(bytes, 16, position)
      (Int16(value), 32)
    }
  }
}