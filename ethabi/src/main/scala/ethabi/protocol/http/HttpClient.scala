package ethabi
package protocol
package http

import cats._
import cats.effect._
import cats.effect.concurrent.Deferred
import cats.implicits._
import io.circe.Decoder
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

      override def doRequest[R: Decoder](request: Request): F[Deferred[F, R]] = {
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
          request  <- requestF
          response <- client.expect[Response](request)
          result   <- response.convertTo[R, F]
          promise  <- Deferred[F, R]
          _ <- promise.complete(result)
        } yield promise

      }
    }

    Resource.liftF[F, HttpClient[F]](httpClient)
  }
}
