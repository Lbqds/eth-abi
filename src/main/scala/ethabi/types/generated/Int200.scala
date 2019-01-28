// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int200(val value: BigInt) extends SolType {
  assert(value.bitLength <= 200)
  override def toString = value.toString
}
object Int200 {
  def apply(value: BigInt): Int200 = new Int200(value)
  implicit lazy val typeInfo: TypeInfo[Int200] = new TypeInfo[Int200] {
    override def name: String = "int200"
    override def isStatic: Boolean = true
    override def encode[U >: Int200](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int200].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int200, Int) = {
      val value = IntType.decode(bytes, 200, position)
      (Int200(value), 32)
    }
  }
}