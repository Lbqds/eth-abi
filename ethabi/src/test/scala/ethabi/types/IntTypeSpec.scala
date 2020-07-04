package ethabi.types

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import ethabi.types.generated._
import ethabi.util.Hex

class IntTypeSpec extends AnyWordSpec with Matchers {
  "test int8 encode" in {
    // fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff4
    Hex.bytes2Hex(TypeInfo[Int8].encode(Int8(BigInt(-12)))) shouldBe "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff4"
  }

  "test int8 decode" in {
    val encoded = Hex.hex2Bytes("fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff4")
    val (result, consumed) = TypeInfo[Int8].decode(encoded, 0)
    result.value.toInt shouldBe -12
    consumed shouldBe 32
  }

  "test int16 encode" in {
    // 0000000000000000000000000000000000000000000000000000000000002710
    Hex.bytes2Hex(TypeInfo[Int16].encode(Int16(BigInt(10000)))) shouldBe "0000000000000000000000000000000000000000000000000000000000002710"
  }

  "test int16 decode" in {
    val encoded = Hex.hex2Bytes("0000000000000000000000000000000000000000000000000000000000002710")
    val (result, consumed) = TypeInfo[Int16].decode(encoded, 0)
    result.value.toInt shouldBe 10000
    consumed shouldBe 32
  }
}
