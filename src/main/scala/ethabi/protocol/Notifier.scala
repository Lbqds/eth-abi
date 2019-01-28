package ethabi.protocol

import akka.actor.{ActorRef, Status}
import akka.stream.{Attributes, Outlet, SourceShape}
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}
import io.circe.Decoder
import ethabi.protocol.Subscription.{Notification, SubscriptionId, UpstreamStopped}
import scala.collection.mutable

class Notifier[T : Decoder](coordinator: ActorRef, request: Request) extends GraphStage[SourceShape[T]] {
  import Notifier._
  private val outlet = Outlet[T]("notifier.out")
  private val queue = mutable.Queue.empty[T]
  override val shape = SourceShape(outlet)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = new GraphStageLogic(shape) {
    private var subscriptionId: Option[SubscriptionId] = None
    implicit def self = stageActor.ref

    setHandler(outlet, new OutHandler {
      override def onPull(): Unit = {
        if (queue.nonEmpty) {
          val element = queue.dequeue()
          push(outlet, element)
        }
      }
    })

    override def preStart(): Unit = {
      val target = getStageActor(handler).ref
      coordinator ! StartSubscribe(target, request)
    }

    override def postStop(): Unit = {
      subscriptionId.foreach(coordinator ! Unsubscribe(_))
    }

    private def handler(receive: (ActorRef, Any)): Unit = {
      receive match {
        case (_, SubscribeSucceed(id)) => subscriptionId = Some(id)
        case (_, notification: Notification) =>
          val element = notification.as[T]
          if (isAvailable(outlet) && queue.isEmpty) push(outlet, element)
          else queue.enqueue(element.asInstanceOf[T])
        case (_, UpstreamStopped) =>
          if (queue.nonEmpty) emitMultiple(outlet, queue.toList, () => completeStage())
          else completeStage()
        case (_, failure: Status.Failure) =>
          if (queue.nonEmpty) emitMultiple(outlet, queue.toList, () => fail(outlet, failure.cause))
          else fail(outlet, failure.cause)
        case _ => // log
      }
    }
  }
}

object Notifier {
  case class StartSubscribe(target: ActorRef, request: Request)
  case class SubscribeSucceed(subscriptionId: SubscriptionId)
  case class Unsubscribe(subscriptionId: SubscriptionId)
}
