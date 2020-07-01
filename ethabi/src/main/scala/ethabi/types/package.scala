package ethabi

package object types {
  private [types] val maxBitLength = 256
  private [types] val maxByteLength = 32

  private [types] def paddedBytes(value: BigInt): Array[Byte] = {
    val paddedValue = if (value.signum == -1) 0xff else 0x00
    Array.fill[Byte](maxByteLength)(paddedValue.toByte)
  }
}
