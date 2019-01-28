package ethabi.types

import org.scalatest.{WordSpec, Matchers}
import ethabi.types.generated._

class IntTypeSpec extends WordSpec with Matchers {
  "test int8 encode" in {
    // fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff4
    Int8.typeInfo.encode(Int8(BigInt(-12))).map("%02x" format _).mkString shouldBe "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff4"
  }

  "test int8 decode" in {
    val encoded = "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff4".sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)
    val (result, consumed) = Int8.typeInfo.decode(encoded, 0)
    result.value.toInt shouldBe -12
    consumed shouldBe 32
  }

  "test int16 encode" in {
    // 0000000000000000000000000000000000000000000000000000000000002710
    Int16.typeInfo.encode(Int16(BigInt(10000))).map("%02x" format _).mkString shouldBe "0000000000000000000000000000000000000000000000000000000000002710"
  }

  "test int16 decode" in {
    val encoded = "0000000000000000000000000000000000000000000000000000000000002710".sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)
    val (result, consumed) = Int16.typeInfo.decode(encoded, 0)
    result.value.toInt shouldBe 10000
    consumed shouldBe 32
  }
}
