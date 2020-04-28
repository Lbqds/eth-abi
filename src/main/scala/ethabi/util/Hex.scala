package ethabi.util

import java.math.BigInteger

object Hex {
  private def removePrefix(hex: String): String = {
    if (hex.startsWith("0x") || hex.startsWith("0X")) hex.slice(2, hex.length)
    else hex
  }

  def hex2Bytes(hex: String): Array[Byte] = {
    val slice = removePrefix(hex)
    slice.toSeq.sliding(2, 2).toArray.map(v => Integer.parseInt(v.toString, 16).toByte)
  }

  def bytes2Hex(bytes: Array[Byte], withPrefix: Boolean = false): String = {
    val hex = bytes.map("%02x" format _).mkString
    if (withPrefix) "0x" + hex
    else hex
  }

  def bigInt2Hex(value: BigInt, withPrefix: Boolean = false): String = {
    // NOTE: remove the leading zeros
    val hex = bytes2Hex(value.toByteArray).replaceFirst("^0+(?!$)", "")
    if (withPrefix) "0x" + hex else hex
  }

  def hex2BigInt(hex: String): BigInt = {
    val bytes = hex2Bytes(hex)
    BigInt(new BigInteger(bytes))
  }

  def int2Hex(value: Int, withPrefix: Boolean = false): String = {
    if (withPrefix) "0x" + value.toHexString
    else value.toHexString
  }

  def hex2Int(hex: String): Int = {
    val slice = removePrefix(hex)
    java.lang.Integer.parseInt(slice, 16)
  }

  def long2Hex(value: Long, withPrefix: Boolean = false): String = {
    if (withPrefix) "0x" + value.toHexString
    else value.toHexString
  }

  def hex2Long(hex: String): Long = {
    val slice = removePrefix(hex)
    java.lang.Long.parseLong(slice, 16)
  }
}
