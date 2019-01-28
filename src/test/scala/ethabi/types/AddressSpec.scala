package ethabi.types

import org.scalatest.{WordSpec, Matchers}
import ethabi.util.Hex

class AddressSpec extends WordSpec with Matchers {
  "test address encode" in {
    val bytes = Array.fill[Byte](20)(0x24)
    // 0000000000000000000000002424242424242424242424242424242424242424
    Hex.bytes2Hex(Address.typeInfo.encode(Address(bytes))) shouldBe "0000000000000000000000002424242424242424242424242424242424242424"
  }

  "test address decode" in {
    val bytes = Array.fill[Byte](20)(0x24)
    val encoded = Hex.hex2Bytes("0000000000000000000000002424242424242424242424242424242424242424")
    val (result, consumed) = Address.typeInfo.decode(encoded, 0)
    result.value shouldBe bytes
    consumed shouldBe encoded.length
  }
}
