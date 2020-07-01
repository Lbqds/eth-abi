package ethabi
package protocol

import io.circe._

/**
 * [[Request.id]] and [[Response.id]], [[Id]] at here
 * because of we overwrite [[Decoder]] for Long at [[implicits]]
 *
 * @param value the id value
 */
final case class Id(value: Long) extends AnyVal

object Id {

  implicit val idDecoder: Decoder[Id] = (c: HCursor) => {
    c.value.asNumber match {
      case None => Left(DecodingFailure("decode to id failed", Nil))
      case Some(number) => Right(Id(number.toLong.get))
    }
  }

  implicit val idEncoder: Encoder[Id] = (id: Id) => Json.fromLong(id.value)
}