package ethabi.types

import org.scalatest.{WordSpec, Matchers}
import ethabi.types.generated._

class IntTypeSpec extends WordSpec with Matchers {
  "test int8 encode" in {
    // fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff4
    TypeInfo[Int8].encode(Int8(BigInt(-12))).map("%02x" format _).mkString shouldBe "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff4"
  }

  "test int8 decode" in {
    val encoded = "fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff4".sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)
    val (result, consumed) = TypeInfo[Int8].decode(encoded, 0)
    result.value.toInt shouldBe -12
    consumed shouldBe 32
  }

  "test int16 encode" in {
    // 0000000000000000000000000000000000000000000000000000000000002710
    TypeInfo[Int16].encode(Int16(BigInt(10000))).map("%02x" format _).mkString shouldBe "0000000000000000000000000000000000000000000000000000000000002710"
  }

  "test int16 decode" in {
    val encoded = "0000000000000000000000000000000000000000000000000000000000002710".sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)
    val (result, consumed) = TypeInfo[Int16].decode(encoded, 0)
    result.value.toInt shouldBe 10000
    consumed shouldBe 32
  }
}
