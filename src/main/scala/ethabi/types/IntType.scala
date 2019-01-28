package ethabi.types

import java.math.BigInteger

object IntType {
  def encode(value: BigInt): Array[Byte] = {
    val encoded = value.toByteArray
    val result = paddedBytes(value)
    Array.copy(encoded, 0, result, maxByteLength - encoded.length, encoded.length)
    result
  }

  def decode(bytes: Array[Byte], length: Int, position: Int): BigInt = {
    val encoded = bytes.slice(position, position + 32)
    val byteLength = length >> 3
    val result = Array.fill[Byte](byteLength + 1)(0)
    result(0) = encoded(0)
    val offset = maxByteLength - byteLength
    Array.copy(encoded, offset, result, 1, byteLength)
    BigInt(new BigInteger(result))
  }
}
