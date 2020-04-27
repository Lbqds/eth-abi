package ethabi.protocol.ws

import akka.NotUsed
import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props, Status}
import akka.http.scaladsl.Http
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.{Message, TextMessage, WebSocketRequest}
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.{Decoder, jawn}
import ethabi.protocol.Notifier.{StartSubscribe, SubscribeSucceed, Unsubscribe}
import ethabi.protocol.Subscription._
import ethabi.protocol.Response._
import ethabi.protocol._
import ethabi.protocol.ws.Client.NewRequest
import scala.concurrent.{Future, Promise}
import scala.collection.mutable
import scala.util.{Failure, Success}

final class Client(url: String)(implicit system: ActorSystem, materializer: ActorMaterializer) extends Service with Subscription {
  import system.dispatcher

  private val listener = system.actorOf(Props(new Listener))
  // TODO: configurable
  private val (requestReceiver, upgradeResponse) = Source.actorRef[Message](bufferSize = 1024, overflowStrategy = OverflowStrategy.dropTail)
    .viaMat(Http().webSocketClientFlow(WebSocketRequest(url)))(Keep.both)
    .to(Sink.actorRef(listener, UpstreamStopped))
    .run()

  upgradeResponse onComplete {
    case Success(upgrade) =>
      if (upgrade.response.status != StatusCodes.SwitchingProtocols)
        throw new RuntimeException(s"connect failed: ${upgrade.response.status}")
    case Failure(exception) => throw new RuntimeException(s"connect failed: $exception")
  }

  // default supervisor strategy is ok
  final class Listener extends Actor {
    private val requests = mutable.Map.empty[Long, Promise[Response]]
    private val subscribers = mutable.Map.empty[SubscriptionId, ActorRef]

    override def receive: Receive = {
      case message: TextMessage.Strict => onMessage(message)
      case UpstreamStopped =>
        subscribers.values.foreach(_ ! UpstreamStopped)
        self ! PoisonPill
      case failure: Status.Failure =>
        subscribers.values.foreach(_ ! failure)
        self ! PoisonPill
      case StartSubscribe(target, request) => onSubscribe(target, request)
      case NewRequest(request, promise) => onNewRequest(request, promise)
      case Unsubscribe(id) => onUnsubscribe(id)
      case _ => // log
    }

    private def onNewRequest(request: Request, promise: Promise[Response]): Unit = {
      requests(request.id) = promise
      val message = TextMessage(request.asJson.toString)
      requestReceiver ! message
    }

    private def onMessage(message: TextMessage.Strict): Unit = {
      val json = jawn.parse(message.text) match {
          case Left(parsingFailure) => throw parsingFailure
          case Right(result) => result
        }
        Decoder[Response].either(Decoder[Notification]).decodeJson(json) match {
          case Left(decodingFailure) => throw decodingFailure
          case Right(result) => result match {
            case Left(response) => onResponse(response)
            case Right(notification) => onNotification(notification)
          }
        }
    }

    private def onSubscribe(target: ActorRef, request: Request): Unit = {
      val promise = Promise[Response]
      self ! NewRequest(request, promise)
      promise.future onComplete {
        case Success(response) => response.as[String] match {
          case Right(Some(id)) => subscribers(id) = target; target ! SubscribeSucceed(id)
          case _ => throw new RuntimeException(s"subscribe failed")
        }
        case Failure(exception) => throw exception
      }
    }

    private def onUnsubscribe(id: String): Unit = {
      subscribers.remove(id)
      val promise = Promise[Response]
      self ! NewRequest(Request.unsubscribe(id), promise)
      promise.future onComplete {
        case Success(_) =>
        case Failure(_) => onUnsubscribe(id)   // try again when failed
      }
    }

    private def onResponse(response: Response): Unit = {
      requests(response.id).trySuccess(response)
      requests.remove(response.id)
    }

    private def onNotification(notification: Notification): Unit = {
      subscribers.get(notification.params.subscription).foreach(_ ! notification)
    }
  }

  override def allowSubscribe: Boolean = true
  override def doRequest(req: Request): Future[Response] = {
    val promise = Promise[Response]
    listener ! NewRequest(req, promise)
    promise.future
  }

  override def subscribeNewHeaders(includeTransactions: Boolean = false): Source[Header, NotUsed] = {
    val request = Request.subscribeNewHeader(includeTransactions)
    Source.fromGraph(new Notifier[Header](listener, request))
  }

  override def subscribeLogs(logQuery: Request.LogQuery): Source[Log, NotUsed] = {
    val request = Request.subscribeLogs(logQuery)
    Source.fromGraph(new Notifier[Log](listener, request))
  }

  override def subscribeNewPendingTransaction(): Source[String, NotUsed] = {
    val request = Request.subscribeNewPendingTransactions()
    Source.fromGraph(new Notifier[String](listener, request))
  }

  override def subscribeSyncStatus(): Source[SyncStatus, NotUsed] = {
    val request = Request.subscribeSyncStatus()
    Source.fromGraph(new Notifier[SyncStatus](listener, request))
  }
}

object Client {
  private case class NewRequest(request: Request, promise: Promise[Response])

  def apply(url: String)(implicit system: ActorSystem, materialzier: ActorMaterializer) = new Client(url)
}
