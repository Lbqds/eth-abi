package ethabi.types

import org.scalatest.{Matchers, WordSpec}
import ethabi.types.generated._

class UintTypeSpec extends WordSpec with  Matchers {
  // 000000000000000000000000000000000000000000000000000000000000000c
  "test uint8 encode" in {
    Uint8.typeInfo.encode(Uint8(BigInt(12))).map("%02x" format _).mkString shouldBe "000000000000000000000000000000000000000000000000000000000000000c"
  }

  "test uint8 decode" in {
    val encoded = "000000000000000000000000000000000000000000000000000000000000000c".sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)
    val (result, consumed) = Uint8.typeInfo.decode(encoded, 0)
    result.value.toInt shouldBe 12
    consumed shouldBe 32
  }

  "test uint16 encode" in {
    // 0000000000000000000000000000000000000000000000000000000000002710
    Uint16.typeInfo.encode(Uint16(BigInt(10000))).map("%02x" format _).mkString shouldBe "0000000000000000000000000000000000000000000000000000000000002710"
  }

  "test uint16 decode" in {
    val encoded = "0000000000000000000000000000000000000000000000000000000000002710".sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)
    val (result, consumed) = Uint16.typeInfo.decode(encoded, 0)
    result.value.toInt shouldBe 10000
    consumed shouldBe 32
  }
}
