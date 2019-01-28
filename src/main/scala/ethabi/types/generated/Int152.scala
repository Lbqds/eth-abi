// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int152(val value: BigInt) extends SolType {
  assert(value.bitLength <= 152)
  override def toString = value.toString
}
object Int152 {
  def apply(value: BigInt): Int152 = new Int152(value)
  implicit lazy val typeInfo: TypeInfo[Int152] = new TypeInfo[Int152] {
    override def name: String = "int152"
    override def isStatic: Boolean = true
    override def encode[U >: Int152](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int152].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int152, Int) = {
      val value = IntType.decode(bytes, 152, position)
      (Int152(value), 32)
    }
  }
}