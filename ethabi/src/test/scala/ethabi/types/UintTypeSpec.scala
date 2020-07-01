package ethabi.types

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import ethabi.types.generated._
import ethabi.util.Hex

class UintTypeSpec extends AnyWordSpec with  Matchers {
  // 000000000000000000000000000000000000000000000000000000000000000c
  "test uint8 encode" in {
    Hex.bytes2Hex(TypeInfo[Uint8].encode(Uint8(BigInt(12)))) shouldBe "000000000000000000000000000000000000000000000000000000000000000c"
  }

  "test uint8 decode" in {
    val encoded = Hex.hex2Bytes("000000000000000000000000000000000000000000000000000000000000000c")
    val (result, consumed) = TypeInfo[Uint8].decode(encoded, 0)
    result.value.toInt shouldBe 12
    consumed shouldBe 32
  }

  "test uint16 encode" in {
    // 0000000000000000000000000000000000000000000000000000000000002710
    Hex.bytes2Hex(TypeInfo[Uint16].encode(Uint16(BigInt(10000)))) shouldBe "0000000000000000000000000000000000000000000000000000000000002710"
  }

  "test uint16 decode" in {
    val encoded = Hex.hex2Bytes("0000000000000000000000000000000000000000000000000000000000002710")
    val (result, consumed) = TypeInfo[Uint16].decode(encoded, 0)
    result.value.toInt shouldBe 10000
    consumed shouldBe 32
  }
}
