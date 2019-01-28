package ethabi.util

import org.scalatest.{WordSpec, Matchers}

class HexSpec extends WordSpec with Matchers {
  "test empty hex to bytes" in {
    val bytes = Hex.hex2Bytes("")
    bytes.length shouldBe 0
  }
}
