package ethabi.util

import scorex.crypto.hash.Keccak256

final case class Hash(bytes: Array[Byte]) {
  assert(bytes.length == 32)
  override def toString: String = Hex.bytes2Hex(bytes, withPrefix = true)
}

object Hash {
  def hash(bytes: Array[Byte]): Hash = Hash(Keccak256.hash(bytes))
  def apply(hex: String): Hash = Hash(Hex.hex2Bytes(hex))
}
