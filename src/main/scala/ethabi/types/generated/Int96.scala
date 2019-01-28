// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Int96(val value: BigInt) extends SolType {
  assert(value.bitLength <= 96)
  override def toString = value.toString
}
object Int96 {
  def apply(value: BigInt): Int96 = new Int96(value)
  implicit lazy val typeInfo: TypeInfo[Int96] = new TypeInfo[Int96] {
    override def name: String = "int96"
    override def isStatic: Boolean = true
    override def encode[U >: Int96](value: U): Array[Byte] = {
      IntType.encode(value.asInstanceOf[Int96].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Int96, Int) = {
      val value = IntType.decode(bytes, 96, position)
      (Int96(value), 32)
    }
  }
}