package ethabi.types

trait TypeInfo[+T <: SolType] {
  def name: String
  def isStatic: Boolean
  def encode[U >: T](value: U): Array[Byte]
  def decode(bytes: Array[Byte], position: Int): (T, Int)
}

object TypeInfo {
  implicit def apply[T <: SolType](implicit info: TypeInfo[T]): TypeInfo[T] = info
}
