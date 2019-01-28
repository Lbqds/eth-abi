package ethabi.types

import java.math.BigInteger
import generated.Uint160
import ethabi.util.Hex

final class Address(val value: Array[Byte]) extends SolType {
  assert(value.length == 20)

  override def toString: String = Hex.bytes2Hex(value, withPrefix = true)
}

object Address {
  val empty = Address(Array.fill[Byte](20)(0))

  def apply(address: String): Address = {
    Address(Hex.hex2Bytes(address))
  }
  def apply(bytes: Array[Byte]): Address = new Address(bytes)

  implicit lazy val typeInfo: TypeInfo[Address] = new TypeInfo[Address] {
    override def name: String = "address"
    override def isStatic: Boolean = true
    override def encode[U >: Address](address: U): Array[Byte] = {
      Uint160.typeInfo.encode(Uint160(BigInt(new BigInteger(address.asInstanceOf[Address].value))))
    }
    override def decode(bytes: Array[Byte], position: Int): (Address, Int) = {
      val (result, consumed) = Uint160.typeInfo.decode(bytes, position)
      (Address(result.value.toByteArray), consumed)
    }
  }
}
