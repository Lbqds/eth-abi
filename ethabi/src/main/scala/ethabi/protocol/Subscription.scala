package ethabi
package protocol

import cats.effect.Sync
import cats.ApplicativeError
import cats.Applicative
import fs2.Stream
import io.circe.Decoder
import io.circe.Json
import io.circe.generic.JsonCodec

object Subscription {

  /**
   * [[SubscriptionResult]] returned after call subscribe* method
   *
   * @param id       subscription id returned from ethereum client
   * @param stream   notification stream
   * @tparam F       effect type
   * @tparam A       notification type
   */
  final case class SubscriptionResult[F[_], A](id: SubscriptionId, stream: Stream[F, A])

  type SubscriptionId = String

  // dummy notification for all subscription
  private[protocol] val dummy = Notification("", "", NotificationParam(Json.Null, ""))

  // keep this struct for auto derived json Encoder/Decoder from circe
  @JsonCodec(decodeOnly = true)
  private[protocol] case class NotificationParam(result: Json, subscription: String)
  @JsonCodec(decodeOnly = true)
  private[protocol] case class Notification(jsonrpc: String, method: String, params: NotificationParam) {
    def subscriptionId: String = params.subscription

    def valid: Boolean = params.subscription != dummy.params.subscription

    def convertTo[T: Decoder, F[_]: Sync]: F[T] = {
      Decoder[T].decodeJson(params.result).fold(
        ApplicativeError[F, Throwable].raiseError,
        Applicative[F].pure
      )
    }
  }
}
