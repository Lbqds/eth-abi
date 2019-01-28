package ethabi.types

// Mark trait for solidity type
trait SolType

trait TypeInfo[+T <: SolType] {
  def name: String
  def isStatic: Boolean
  def encode[U >: T](value: U): Array[Byte]
  def decode(bytes: Array[Byte], position: Int): (T, Int)
}
