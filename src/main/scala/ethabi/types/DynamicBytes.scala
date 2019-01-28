package ethabi.types

import generated.Uint256
import ethabi.util.Hex

final class DynamicBytes(val value: Array[Byte]) extends SolType {
  override def toString: String = Hex.bytes2Hex(value, withPrefix = true)
}

object DynamicBytes {
  def apply(value: Array[Byte]): DynamicBytes = new DynamicBytes(value)

  // encodedLength don't include prefix 32 bytes length
  private def encodedLength(length: Int): Int = {
    if (length % 32 == 0) {
      length
    } else {
      (length / 32) * 32 + 32
    }
  }

  def encode(value: Array[Byte]): Array[Byte] = {
    val length = Uint256(BigInt(value.length))
    val lengthEncoded = Uint256.typeInfo.encode(length)
    val totalLength = encodedLength(value.length) + 32
    val result = Array.fill[Byte](totalLength)(0)
    Array.copy(lengthEncoded, 0, result, 0, 32)
    Array.copy(value, 0, result, 32, value.length)
    result
  }

  def decode(bytes: Array[Byte], position: Int): (Array[Byte], Int) = {
    val (result, consumed) = Uint256.typeInfo.decode(bytes, position)
    val offset = position + consumed
    val length = result.value.toInt
    val encodedLen = encodedLength(length)
    (bytes.slice(offset, offset + length), consumed + encodedLen)
  }

  implicit lazy val typeInfo: TypeInfo[DynamicBytes] = new TypeInfo[DynamicBytes] {
    override def name: String = "bytes"
    override def isStatic: Boolean = false
    override def encode[U >: DynamicBytes](value: U): Array[Byte] = {
      DynamicBytes.encode(value.asInstanceOf[DynamicBytes].value)
    }
    override def decode(bytes: Array[Byte], position: Int): (DynamicBytes, Int) = {
      val (result, consumed) = DynamicBytes.decode(bytes, position)
      (DynamicBytes(result), consumed)
    }
  }
}
