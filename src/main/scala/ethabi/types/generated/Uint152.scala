// AUTO GENERATED, DO NOT EDIT

package ethabi
package types
package generated
final class Uint152(val value: BigInt) extends SolType {
  assert(value.bitLength <= 152)
  override def toString = value.toString
}
object Uint152 {
  def apply(value: BigInt): Uint152 = new Uint152(value)
  implicit lazy val typeInfo: TypeInfo[Uint152] = new TypeInfo[Uint152] {
    override def name: String = "uint152"
    override def isStatic: Boolean = true
    override def encode[U >: Uint152](value: U): Array[Byte] = {
      UintType.encode(value.asInstanceOf[Uint152].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (Uint152, Int) = {
      val value = UintType.decode(bytes, 152, position)
      (Uint152(value), 32)
    }
  }
}