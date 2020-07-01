package ethabi
package protocol
package http

import cats._
import cats.effect._
import cats.effect.concurrent.Deferred
import cats.implicits._
import io.circe.Decoder
import io.circe.generic.auto._
import org.http4s.client.jdkhttpclient.JdkHttpClient
import org.http4s.EntityDecoder
import org.http4s.EntityEncoder
import org.http4s.Header
import org.http4s.Headers
import org.http4s.Method
import org.http4s.Uri
import org.http4s.circe
import org.http4s.{ Request => HttpRequest }

abstract class HttpClient[F[_]] extends Client[F]

object HttpClient {
  def apply[F[_]: ConcurrentEffect](endpoint: String)(implicit CS: ContextShift[F]): Resource[F, HttpClient[F]] = {
    val httpClient = for {
      client <- JdkHttpClient.simple[F]
    } yield new HttpClient[F] {

      override def doRequest[R: Decoder](request: Request): F[Deferred[F, Option[R]]] = {
        implicit val responseDecoder: EntityDecoder[F, Response] = circe.jsonOf[F, Response]
        implicit val requestEncoder: EntityEncoder[F, Request] = circe.jsonEncoderOf[F, Request]

        val requestF: F[HttpRequest[F]] = Uri.fromString(endpoint).fold(
          ApplicativeError[F, Throwable].raiseError,
          uri => Applicative[F].pure(HttpRequest[F](
            method = Method.POST,
            headers = Headers.of(Header("Content-Type", "application/json")),
            uri = uri,
          ).withEntity(request))
        )

        for {
          request <- requestF
          response <- client.expect[Response](request)
          result <- response.convertTo[R, F]
          promise <- Deferred[F, Option[R]]
          _ <- promise.complete(result)
        } yield promise

      }
    }

    Resource.liftF[F, HttpClient[F]](httpClient)
  }
}

/*
object HttpClientTest extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    import HttpClient._
    apply[IO]("http://127.0.0.1:8545").use { client =>
      for {
        p <- client.clientVersion
        cVersion <- p.get
        _ <- IO.delay(println(s"client version: $cVersion"))
        p1 <- client.netVersion
        nVersion <- p1.get
        _ <- IO.delay(println(s"net version: $nVersion"))
      } yield ExitCode.Success
    }
  }
}
 */