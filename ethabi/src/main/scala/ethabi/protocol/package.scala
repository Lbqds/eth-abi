package ethabi

import cats.ApplicativeError
import cats.Applicative
import cats.MonadError
import cats.effect.Timer
import retry.RetryPolicy
import retry.RetryDetails

package object protocol {

  // TODO: replace with kind-projector
  type ApplicativeErrorL[F[_]] = ApplicativeError[F, Throwable]

  private[protocol] def assertNotNone[F[_]: ApplicativeErrorL, T](tag: String, result: Option[T]): F[T] = {
    val exception = new RuntimeException(s"expected $tag, but have none")
    result.fold[F[T]](
      ApplicativeError[F, Throwable].raiseError(exception))(t => Applicative[F].pure(t))
  }

  type MonadErrorL[F[_]] = MonadError[F, Throwable]

  def retryUntil[F[_]: MonadErrorL: Timer, T](
    tag: String,
    strategy: RetryPolicy[F],
    task: => F[T],
    pred: T => Boolean
  ): F[T] = {
    val onFailure: (T, RetryDetails) => F[Unit] = {
      case (_, RetryDetails.GivingUp(retries, delay)) => ApplicativeError[F, Throwable].raiseError(
        new RuntimeException(s"$tag retry give up, total retries: $retries, total delay: ${delay.toSeconds} seconds")
      )
      case (_, RetryDetails.WillDelayAndRetry(_, _, _)) => Applicative[F].unit  // TODO: log here
    }
    retry.retryingM[T].apply[F](strategy, pred, onFailure)(task)
  }
}
