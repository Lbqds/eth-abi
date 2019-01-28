// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int248(val value: BigInt) extends SolType {
  assert(value.bitLength <= 248)
  override def toString = value.toString
}
object Int248 {
  def apply(value: BigInt): Int248 = new Int248(value)
  implicit lazy val typeInfo: TypeInfo[Int248] = new TypeInfo[Int248] {
    override def name: String = "int248"
    override def isStatic: Boolean = true
    override def encode[U >: Int248](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int248].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int248, Int) = {
      val value = IntType.decode(bytes, 248, position)
      (Int248(value), 32)
    }
  }
}