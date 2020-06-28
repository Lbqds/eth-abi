package ethabi.types

import org.scalatest.{WordSpec, Matchers}
import ethabi.util.Hex

class StringTypeSpec extends WordSpec with Matchers {
  "test utf8 string encode" in {
    val value = StringType("以太坊")
    // 0000000000000000000000000000000000000000000000000000000000000009 +
    // e4bba5e5a4aae59d8a0000000000000000000000000000000000000000000000
    Hex.bytes2Hex(TypeInfo[StringType].encode(value)) shouldBe "0000000000000000000000000000000000000000000000000000000000000009e4bba5e5a4aae59d8a0000000000000000000000000000000000000000000000"
  }

  "test utf8 string decode" in {
    val encoded = Hex.hex2Bytes("0000000000000000000000000000000000000000000000000000000000000009e4bba5e5a4aae59d8a0000000000000000000000000000000000000000000000")
    val (result, consumed) = TypeInfo[StringType].decode(encoded, 0)
    result.value shouldBe "以太坊"
    consumed shouldBe encoded.length
  }
}
