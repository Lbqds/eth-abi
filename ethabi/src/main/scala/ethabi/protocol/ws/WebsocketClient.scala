package ethabi
package protocol
package ws

import cats._
import cats.effect._
import cats.effect.concurrent._
import cats.effect.implicits._
import cats.implicits._
import fs2.Stream
import fs2.concurrent.Topic
import io.circe.Decoder
import io.circe.jawn
import org.http4s.Uri
import org.http4s.client.jdkhttpclient._

abstract class WebsocketClient[F[_]] extends Client[F, Deferred] with Subscriber[F] {
  private[ws] def terminate: F[Unit]
}

object WebsocketClient {

  import Subscription.SubscriptionResult
  import Subscription.SubscriptionId

  def apply[F[_]: ConcurrentEffect](endpoint: String)(implicit CS: ContextShift[F]): Resource[F, WebsocketClient[F]] = {

    val resource: F[Resource[F, WSConnectionHighLevel[F]]] = JdkWSClient.simple[F].flatMap { wsClient =>
      Uri.fromString(endpoint).map(uri => wsClient.connectHighLevel(WSRequest(uri)))
        .fold(err => ApplicativeError[F, Throwable].raiseError(err), resource => Sync[F].delay(resource))
    }

    type CompletedCallback = Response => F[Unit]

    def dispatch(
      connection: WSConnectionHighLevel[F],
      requestMap: Ref[F, Map[Long, CompletedCallback]],
      subscriptionMap: Ref[F, Map[SubscriptionId, Topic[F, Subscription.Notification]]]
    ): Stream[F, Nothing] = {
      Stream.eval(connection.receive).flatMap {

        case Some(WSFrame.Text(data, true)) =>

          def onResponse(id: Long, resp: Response): F[Unit] = {
            requestMap.modify[Option[CompletedCallback]] { map =>
              map.get(id) match {
                case Some(callback) => (map - id, Some(callback))
                case _ => (map, None)
              }
            }.flatMap(_.fold[F[Unit]](Sync[F].unit)(_.apply(resp)))
          }

          def onNotify(notification: Subscription.Notification): F[Unit] = {
            for {
              map <- subscriptionMap.get
              _   <- map.get(notification.subscriptionId).fold(Sync[F].unit)(_.publish1(notification))
            } yield ()
          }

          jawn.parse(data).fold(Stream.raiseError[F], json => {
            val decoder = Decoder[Response].either(Decoder[Subscription.Notification])
            decoder.decodeJson(json)
              .fold(Stream.raiseError[F], _.fold(
                resp => Stream.eval_(onResponse(resp.id.value, resp)),
                notification => Stream.eval_(onNotify(notification))
              ))
          })

        case Some(frame) => Stream.raiseError[F](new RuntimeException(s"unexpected frame $frame"))

        case None => Stream.empty

      }.handleErrorWith { err =>
        Stream.eval_(Sync[F].delay(err.printStackTrace())) ++   // ignore current error and continue, NOTE: it is necessary to handle
        dispatch(connection, requestMap, subscriptionMap)       // error in `Stream` level, stream will stop if handle error in `F` level
      }
    }

    val newWsClient: WSConnectionHighLevel[F] => F[WebsocketClient[F]] = conn => for {
      requestId       <- Ref.of[F, Long](0)
      requestMap      <- Ref[F].of(Map.empty[Long, CompletedCallback])
      subscriptionMap <- Ref[F].of(Map.empty[SubscriptionId, Topic[F, Subscription.Notification]])
      fiber           <- dispatch(conn, requestMap, subscriptionMap).repeat.compile.drain.start
    } yield new WebsocketClient[F] {

      override def terminate: F[Unit] = fiber.cancel

      override def doRequest[R: Decoder](request: Request): F[Deferred[F, R]] = {
        def nextId: F[Long] = requestId.getAndUpdate(_ + 1)

        for {
          id      <- nextId
          promise <- Deferred[F, R]
          _       <- conn.send(fromRequest(request.withId(id)))
          _       <- requestMap.update(_.updated(id, _.convertTo[R, F].flatMap(promise.complete)))
        } yield promise
      }

      private def subscribeHelper[RESP: Decoder](request: Request, queueSize: Int = 64): F[SubscriptionResult[F, RESP]] = {
        val result = for {
          promise        <- doRequest[String](request)
          subscriptionId <- promise.get
          topic          <- Topic[F, Subscription.Notification](Subscription.dummy)
          _              <- subscriptionMap.update(_.updated(subscriptionId, topic))
        } yield (subscriptionId, topic.subscribe(queueSize))    // TODO: configurable
        result.map { case (id, stream) =>
          SubscriptionResult(id, stream.filter(_.valid).evalMap(_.convertTo[RESP, F]))
        }
      }

      override def subscribeLogs(query: Request.LogQuery): F[SubscriptionResult[F, Response.Log]] = {
        subscribeHelper[Response.Log](Request.subscribeLogs(query))
      }

      override def subscribeNewHeaders(): F[SubscriptionResult[F, Response.Header]] = {
        subscribeHelper[Response.Header](Request.subscribeNewHeader())
      }

      override def subscribeNewPendingTransactions(): F[SubscriptionResult[F, String]] = {
        subscribeHelper[String](Request.subscribeNewPendingTransactions())
      }

      override def subscribeSyncStatus(): F[SubscriptionResult[F, Response.SyncStatus]] = {
        subscribeHelper[Response.SyncStatus](Request.subscribeSyncStatus())
      }

      override def unsubscribe(subscriptionId: String): F[Deferred[F, Boolean]] = {
        // send unsubscribe request first, and ignore the response
        for {
          promise <- doRequest[Boolean](Request.unsubscribe(subscriptionId))
          _       <- subscriptionMap.update(_ - subscriptionId)
        } yield promise
      }
    }

    Resource.suspend(resource).flatMap { conn =>
      Resource.make(newWsClient(conn))(_.terminate)
    }
  }

  private def fromRequest(request: Request): WSDataFrame = WSFrame.Text(request.toJson)
}
