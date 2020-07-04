package ethabi.types

import java.math.BigInteger

object UintType {
  def encode(value: BigInt): Array[Byte] = {
    val encoded = if (value.bitLength == maxBitLength) {
      val bytes = new Array[Byte](maxByteLength)
      Array.copy(value.toByteArray, 1, bytes, 0, maxByteLength)
      bytes
    } else value.toByteArray
    val result = Array.fill[Byte](maxByteLength)(0)
    Array.copy(encoded, 0, result, maxByteLength - encoded.length, encoded.length)
    result
  }

  def decode(bytes: Array[Byte], length: Int, position: Int): BigInt = {
    val encoded = bytes.slice(position, position + 32)
    val byteLength = length >> 3
    val result = Array.fill[Byte](byteLength)(0)
    val offset = maxByteLength - byteLength
    Array.copy(encoded, offset, result, 0, byteLength)
    BigInt(new BigInteger(result))
  }
}
