package ethabi.types

object StaticBytes {
  def encode(value: Array[Byte], length: Int): Array[Byte] = {
    val result = Array.fill[Byte](maxByteLength)(0)
    Array.copy(value, 0, result, 0, length)
    result
  }

  def decode(bytes: Array[Byte], length: Int, position: Int): Array[Byte] = {
    val encoded = bytes.slice(position, position + 32)
    val result = Array.fill[Byte](length)(0)
    Array.copy(encoded, 0, result, 0, length)
    result
  }
}
