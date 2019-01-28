package ethabi.protocol

import akka.NotUsed
import akka.stream.scaladsl.Source
import io.circe.{Decoder, Json}
import ethabi.protocol.Request.LogQuery
import ethabi.protocol.Response._

private [protocol] trait Subscription { self: Service =>
  def subscribeNewHeaders(includeTransactions: Boolean = false): Source[Header, NotUsed]
  def subscribeLogs(logQuery: LogQuery): Source[Log, NotUsed]
  def subscribeNewPendingTransaction(): Source[String, NotUsed]
  def subscribeSyncStatus(): Source[SyncStatus, NotUsed]
}

object Subscription {
  type SubscriptionId = String
  private [protocol] case object UpstreamStopped

  private [protocol] case class NotificationParam(result: Json, subscription: String)
  private [protocol] case class Notification(jsonrpc: String, method: String, params: NotificationParam) {
    def as[T : Decoder]: T = {
      val decoder = implicitly[Decoder[T]]
      decoder.decodeJson(params.result) match {
        case Left(decodingFailure) => throw decodingFailure
        case Right(value) => value
      }
    }
  }
}
