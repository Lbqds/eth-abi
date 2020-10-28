package ethabi
package protocol

import cats.effect.concurrent.Deferred
import ethabi.protocol.Request.LogQuery
import ethabi.protocol.Response._
import Subscription.SubscriptionResult
import Subscription.SubscriptionId

trait Subscriber[F[_]] {
  /**
   * subscribe new headers from ethereum client
   *
   * @return [[Subscription.SubscriptionResult]]
   */
  def subscribeNewHeaders(): F[SubscriptionResult[F, Header]]

  /**
   * subscribe logs from ethereum client with [[Request.LogQuery]]
   *
   * @param logQuery refer to [[Request.LogQuery]]
   * @return [[Subscription.SubscriptionResult]]
   */
  def subscribeLogs(logQuery: LogQuery): F[SubscriptionResult[F, Log]]

  /**
   * subscribe new pending transactions from ethereum
   *
   * @return [[Subscription.SubscriptionResult]]
   */
  def subscribeNewPendingTransactions(): F[SubscriptionResult[F, String]]

  /**
   * subscribe ethereum block sync status
   *
   * @return [[Subscription.SubscriptionResult]]
   */
  def subscribeSyncStatus(): F[SubscriptionResult[F, SyncStatus]]

  /**
   * cancel previous subscription
   *
   * @param id subscription id
   * @return true if cancel succeed, false otherwise; client won't receive notification no matter if succeed
   */
  def unsubscribe(id: SubscriptionId): F[Deferred[F, Boolean]]
}


