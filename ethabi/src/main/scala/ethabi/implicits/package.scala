package ethabi

import io.circe.Decoder
import io.circe.Encoder
import io.circe.Json
import io.circe.HCursor
import ethabi.util._
import ethabi.types.Address
import ethabi.types.generated.Bytes32

package object implicits {
  implicit val hashDecoder: Decoder[Hash] = (c: HCursor) => {
    c.value.as[String].map(Hash.apply)
  }

  implicit val bytesDecoder: Decoder[Array[Byte]] = (c: HCursor) => {
    c.value.as[String].map(Hex.hex2Bytes)
  }

  implicit val bytes32Decoder: Decoder[Bytes32] = (c: HCursor) => {
    c.value.as[String].map(Bytes32.from)
  }

  // decode hex string to int e.g. "0x10" => 16
  implicit val intDecoder: Decoder[Int] = (c: HCursor) => {
    c.value.as[String].map(Hex.hex2Int)
  }

  implicit val longDecoder: Decoder[Long] = (c: HCursor) => {
    c.value.as[String].map(Hex.hex2Long)
  }

  implicit val addressDecoder: Decoder[Address] = (c: HCursor) => {
    c.value.as[String].map(Address.from)
  }

  implicit val bigIntDecoder: Decoder[BigInt] = (c: HCursor) => {
    c.value.as[String].map(Hex.hex2BigInt)
  }

  implicit val addrListDecoder: Decoder[List[Address]] = (c: HCursor) => {
    c.value.as[List[String]].map(_.map(Address.from))
  }

  implicit val hashListDecoder: Decoder[List[Hash]] = (c: HCursor) => {
    c.value.as[List[String]].map(_.map(Hash.apply))
  }

  implicit val addressEncoder: Encoder[Address] = (addr: Address) => Json.fromString(addr.toString)

  implicit val hashEncoder: Encoder[Hash] = (h: Hash) => Json.fromString(h.toString)

  implicit val bytesEncoder: Encoder[Array[Byte]] = (b: Array[Byte]) =>
    Json.fromString(Hex.bytes2Hex(b, withPrefix = true))

  implicit val bytes32Encoder: Encoder[Bytes32] = (b: Bytes32) =>
    Json.fromString(Hex.bytes2Hex(b.value, withPrefix = true))

  implicit val bigIntEncoder: Encoder[BigInt] = (v: BigInt) => Json.fromString(Hex.bigInt2Hex(v, withPrefix = true))

  implicit val intEncoder: Encoder[Int] = (v: Int) => Json.fromString(Hex.int2Hex(v, withPrefix = true))

  implicit val longEncoder: Encoder[Long] = (v: Long) => Json.fromString(Hex.long2Hex(v, withPrefix = true))

  implicit val listAddrEncoder: Encoder[List[Address]] = (addrs: List[Address]) =>
    Json.fromValues(addrs.map(addr => Json.fromString(addr.toString)))

  implicit val topicEncoder: Encoder[Either[Option[Bytes32], List[Bytes32]]] = {
    case Left(v) => encode(v)
    case Right(v) => encode(v)
  }

  implicit def encode[T: Encoder](value: T): Json = Encoder[T].apply(value)
}
