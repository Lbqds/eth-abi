package ethabi.types

import org.scalatest.{Matchers, WordSpec}
import ethabi.types.generated._

class UintTypeSpec extends WordSpec with  Matchers {
  // 000000000000000000000000000000000000000000000000000000000000000c
  "test uint8 encode" in {
    TypeInfo[Uint8].encode(Uint8(BigInt(12))).map("%02x" format _).mkString shouldBe "000000000000000000000000000000000000000000000000000000000000000c"
  }

  "test uint8 decode" in {
    val encoded = "000000000000000000000000000000000000000000000000000000000000000c".sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)
    val (result, consumed) = TypeInfo[Uint8].decode(encoded, 0)
    result.value.toInt shouldBe 12
    consumed shouldBe 32
  }

  "test uint16 encode" in {
    // 0000000000000000000000000000000000000000000000000000000000002710
    TypeInfo[Uint16].encode(Uint16(BigInt(10000))).map("%02x" format _).mkString shouldBe "0000000000000000000000000000000000000000000000000000000000002710"
  }

  "test uint16 decode" in {
    val encoded = "0000000000000000000000000000000000000000000000000000000000002710".sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)
    val (result, consumed) = TypeInfo[Uint16].decode(encoded, 0)
    result.value.toInt shouldBe 10000
    consumed shouldBe 32
  }
}
