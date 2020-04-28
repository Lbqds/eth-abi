package ethabi.protocol.http

import akka.actor.ActorSystem
import akka.http.scaladsl._
import akka.http.scaladsl.model._
import akka.stream.Materializer
import akka.util.ByteString
import io.circe.jawn.decode
import io.circe.generic.auto._
import io.circe.syntax._
import ethabi.protocol.{Request, Response, Service}
import scala.concurrent.Future

final class Client(url: String)(implicit system: ActorSystem, materializer: Materializer) extends Service {
  import system.dispatcher

  override def allowSubscribe: Boolean = false
  override def doRequest(req: Request): Future[Response] = {
    val entity = HttpEntity(ContentTypes.`application/json`, req.asJson.toString)
    val resp = Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = url, entity = entity))
    resp.flatMap { response =>
      response.entity.dataBytes.runFold(ByteString(""))(_ ++ _).map { raw =>
        decode[Response](raw.utf8String).getOrElse(throw new NoSuchElementException("decode response error"))
      }
    }
  }
}

object Client {
  def apply(url: String)(implicit system: ActorSystem, materializer: Materializer) = new Client(url)
}
