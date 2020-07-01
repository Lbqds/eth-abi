package ethabi

import io.circe.{Decoder, HCursor}
import ethabi.util._

package object implicits {
  implicit val hashDecoder: Decoder[Hash] = (c: HCursor) => {
    c.value.as[String].map(Hash.apply)
  }

  implicit val bytesDecoder: Decoder[Array[Byte]] = (c: HCursor) => {
    c.value.as[String].map(Hex.hex2Bytes)
  }
}
