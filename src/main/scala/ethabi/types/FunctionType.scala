package ethabi.types

import generated.Bytes24
import ethabi.util.Hex

final class FunctionType(val selector: Array[Byte], val address: Address) extends SolType {
  override def toString: String = Hex.bytes2Hex(selector ++ address.value, withPrefix = true)
}

object FunctionType {
  def apply(selector: Array[Byte], address: Address): FunctionType = new FunctionType(selector, address)

  def encode(function: FunctionType): Array[Byte] = {
    val bytes = Array.fill[Byte](24)(0)
    Array.copy(function.selector, 0, bytes, 0, 4)
    Array.copy(function.address.value, 0, bytes, 4, 20)
    Bytes24.typeInfo.encode(Bytes24(bytes))
  }

  def decode(bytes: Array[Byte], position: Int): (FunctionType, Int) = {
    val (result, consumed) = Bytes24.typeInfo.decode(bytes, position)
    (FunctionType(result.value.slice(0, 4), Address(result.value.slice(4, 24))), consumed)
  }

  implicit lazy val typeInfo: TypeInfo[FunctionType] = new TypeInfo[FunctionType] {
    override def name: String = "function"
    override def isStatic: Boolean = true
    override def encode[U >: FunctionType](value: U): Array[Byte] = {
      FunctionType.encode(value.asInstanceOf[FunctionType])
    }
    override def decode(bytes: Array[Byte], position: Int): (FunctionType, Int) = {
      FunctionType.decode(bytes, position)
    }
  }
}
