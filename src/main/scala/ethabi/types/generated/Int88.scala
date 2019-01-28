// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int88(val value: BigInt) extends SolType {
  assert(value.bitLength <= 88)
  override def toString = value.toString
}
object Int88 {
  def apply(value: BigInt): Int88 = new Int88(value)
  implicit lazy val typeInfo: TypeInfo[Int88] = new TypeInfo[Int88] {
    override def name: String = "int88"
    override def isStatic: Boolean = true
    override def encode[U >: Int88](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int88].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int88, Int) = {
      val value = IntType.decode(bytes, 88, position)
      (Int88(value), 32)
    }
  }
}