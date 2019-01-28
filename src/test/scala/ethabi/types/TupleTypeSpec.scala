package ethabi.types

import org.scalatest.{Matchers, WordSpec}
import ethabi.types.generated._
import ethabi.util.Hex

class TupleTypeSpec extends WordSpec with Matchers {
  import Uint256._
  import Address._
  import DynamicBytes._

  val number = Uint256(BigInt(10000))
  val address = Address(Array.fill[Byte](20)(0x24))
  val bytes = DynamicBytes(Array.fill[Byte](20)(0x13))

  "test tuple3(uint256, address, bytes) encode" in {
    val tuple = TupleType3[Uint256, Address, DynamicBytes](number, address, bytes)
    val tupleTypeInfo = implicitly[TypeInfo[TupleType3[Uint256, Address, DynamicBytes]]]
    // 0000000000000000000000000000000000000000000000000000000000002710 +
    // 0000000000000000000000002424242424242424242424242424242424242424 +
    // 0000000000000000000000000000000000000000000000000000000000000060 +
    // 0000000000000000000000000000000000000000000000000000000000000014 +
    // 1313131313131313131313131313131313131313000000000000000000000000
    Hex.bytes2Hex(tupleTypeInfo.encode(tuple)) shouldBe "00000000000000000000000000000000000000000000000000000000000027100000000000000000000000002424242424242424242424242424242424242424000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000000141313131313131313131313131313131313131313000000000000000000000000"
  }

  "test tuple3(uint256, address, bytes) decode" in {
    val encoded = Hex.hex2Bytes("00000000000000000000000000000000000000000000000000000000000027100000000000000000000000002424242424242424242424242424242424242424000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000000141313131313131313131313131313131313131313000000000000000000000000")
    val tupleTypeInfo = implicitly[TypeInfo[TupleType3[Uint256, Address, DynamicBytes]]]
    val (result, consumed) = tupleTypeInfo.decode(encoded, 0)
    result._1.value.toInt shouldBe number.value.toInt
    result._2.value shouldBe address.value
    result._3.value shouldBe bytes.value
    consumed shouldBe encoded.length
  }

  "test tuple3(tuple2(bytes, address), uint256, bytes) encode" in {
    val tuple2 = TupleType2[DynamicBytes, Address](bytes, address)
    val tuple3 = TupleType3[TupleType2[DynamicBytes, Address], Uint256, DynamicBytes](tuple2, number, bytes)
    val tupleTypeInfo = implicitly[TypeInfo[TupleType3[TupleType2[DynamicBytes, Address], Uint256, DynamicBytes]]]
    // 0000000000000000000000000000000000000000000000000000000000000060 +
    // 0000000000000000000000000000000000000000000000000000000000002710 +
    // 00000000000000000000000000000000000000000000000000000000000000e0 +
    // 0000000000000000000000000000000000000000000000000000000000000040 +
    // 0000000000000000000000002424242424242424242424242424242424242424 +
    // 0000000000000000000000000000000000000000000000000000000000000014 +
    // 1313131313131313131313131313131313131313000000000000000000000000 +
    // 0000000000000000000000000000000000000000000000000000000000000014 +
    // 1313131313131313131313131313131313131313000000000000000000000000
    Hex.bytes2Hex(tupleTypeInfo.encode(tuple3)) shouldBe "0000000000000000000000000000000000000000000000000000000000000060000000000000000000000000000000000000000000000000000000000000271000000000000000000000000000000000000000000000000000000000000000e0000000000000000000000000000000000000000000000000000000000000004000000000000000000000000024242424242424242424242424242424242424240000000000000000000000000000000000000000000000000000000000000014131313131313131313131313131313131313131300000000000000000000000000000000000000000000000000000000000000000000000000000000000000141313131313131313131313131313131313131313000000000000000000000000"
  }

  "test tuple3(tuple2(bytes, address), uint256, bytes) decode" in {
    val encoded = Hex.hex2Bytes("0000000000000000000000000000000000000000000000000000000000000060000000000000000000000000000000000000000000000000000000000000271000000000000000000000000000000000000000000000000000000000000000e0000000000000000000000000000000000000000000000000000000000000004000000000000000000000000024242424242424242424242424242424242424240000000000000000000000000000000000000000000000000000000000000014131313131313131313131313131313131313131300000000000000000000000000000000000000000000000000000000000000000000000000000000000000141313131313131313131313131313131313131313000000000000000000000000")
    val tupleTypeInfo = implicitly[TypeInfo[TupleType3[TupleType2[DynamicBytes, Address], Uint256, DynamicBytes]]]
    val (result, consumed) = tupleTypeInfo.decode(encoded, 0)
    result._1._1.value shouldBe bytes.value
    result._1._2.value shouldBe address.value
    result._2.value.toInt shouldBe number.value.toInt
    result._3.value shouldBe bytes.value
    consumed shouldBe encoded.length
  }
}
