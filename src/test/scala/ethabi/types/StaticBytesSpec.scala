package ethabi.types

import org.scalatest.{Matchers, WordSpec}
import ethabi.types.generated._
import ethabi.util.Hex

class StaticBytesSpec extends WordSpec with Matchers {
  "test bytes5 encode" in {
    val bytes = Array[Byte](0x01, 0x02, 0x03, 0x04, 0x05)
    // 0102030405000000000000000000000000000000000000000000000000000000
    Hex.bytes2Hex(TypeInfo[Bytes5].encode(Bytes5(bytes))) shouldBe "0102030405000000000000000000000000000000000000000000000000000000"
  }

  "test bytes5 decode" in {
    val bytes = Array[Byte](0x01, 0x02, 0x03, 0x04, 0x05)
    val encoded = Hex.hex2Bytes("0102030405000000000000000000000000000000000000000000000000000000")
    val (result, consumed) = TypeInfo[Bytes5].decode(encoded, 0)
    result.value shouldBe bytes
    consumed shouldBe 32
  }
}
