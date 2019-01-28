// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int144(val value: BigInt) extends SolType {
  assert(value.bitLength <= 144)
  override def toString = value.toString
}
object Int144 {
  def apply(value: BigInt): Int144 = new Int144(value)
  implicit lazy val typeInfo: TypeInfo[Int144] = new TypeInfo[Int144] {
    override def name: String = "int144"
    override def isStatic: Boolean = true
    override def encode[U >: Int144](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int144].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int144, Int) = {
      val value = IntType.decode(bytes, 144, position)
      (Int144(value), 32)
    }
  }
}