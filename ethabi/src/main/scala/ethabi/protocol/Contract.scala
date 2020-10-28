package ethabi
package protocol

import cats.Applicative
import cats.implicits._
import cats.effect.concurrent._
import cats.effect._
import ethabi.types._
import ethabi.types.generated.Bytes32
import ethabi.util._
import ws.WebsocketClient
import Request._
import Response._
import Subscription.SubscriptionResult
import retry.RetryPolicies
import scala.concurrent.duration._

trait Contract[F[_]] {

  def isDeployed: F[Boolean]

  // get underling client, which can used to call all ethereum jsonrpc api
  def client: F[WebsocketClient[F]]

  def subscriber: F[Subscriber[F]]

  // get current contract creator if exist
  def creator: F[Option[Address]]

  // get current contract address, it will be set after contract deploy succeed
  def address: F[Option[Address]]

  /**
   * deploy contract to ethereum, [[address]] will be set after deploy succeed,
   * and [[creator]] will be set with CallArgs.sender
   *
   * @param args deploy transaction data
   * @return     deploy transaction hash, which can used to get receipt if deploy failed
   */
  def deploy(args: CallArgs): F[Deferred[F, Hash]]

  // load contract with address, `address` will be set with `contractAddress`
  def load(contractAddress: Address): F[Unit]

  // call contract method by eth_sendTransaction
  def sendTransaction(args: CallArgs): F[Deferred[F, Hash]]

  // call contract method by eth_call
  def call(args: CallArgs): F[Deferred[F, Array[Byte]]]

  // subscribe contract event logs
  def subscribeLogs(topic: Bytes32): F[SubscriptionResult[F, Log]]
}

object Contract {

  def apply[F[_]: ConcurrentEffect: Timer](endpoint: String)(implicit CS: ContextShift[F]): Resource[F, Contract[F]] = {

    val newContract: WebsocketClient[F] => F[Contract[F]] = cli => for {
      contractorCreatorR <- Ref.of[F, Option[Address]](None)
      contractAddressR <- Ref.of[F, Option[Address]](None)
    } yield new Contract[F] {

      override def isDeployed: F[Boolean] = contractAddressR.get.map(_.isDefined)

      override def creator: F[Option[Address]] = contractorCreatorR.get

      override def address: F[Option[Address]] = contractAddressR.get

      override def client: F[WebsocketClient[F]] = Applicative[F].pure(cli)

      override def subscriber: F[Subscriber[F]] = Applicative[F].pure(cli)

      override def load(contractAddress: Address): F[Unit] = contractAddressR.set(Some(contractAddress))

      override def deploy(args: CallArgs): F[Deferred[F, Hash]] = for {
        _       <- contractorCreatorR.set(Some(args.sender))
        txHashP <- cli.sendTransaction(Request.Transaction.deployTransaction(args))
        txHash  <- txHashP.get
        retryPolicies = RetryPolicies.limitRetries[F](5).join(RetryPolicies.constantDelay[F](5 seconds))
        receipt <- retryUntil[F, Option[TransactionReceipt]](
          "wait deploy contract",
          retryPolicies,
          cli.getTransactionReceipt(txHash).flatMap(_.get),
          _.isDefined
        )
        address <- assertNotNone[F, Address]("contract address", receipt.get.contractAddress)
        _       <- contractAddressR.set(Some(address))
      } yield txHashP

      override def sendTransaction(args: CallArgs): F[Deferred[F, Hash]] = for {
        address <- contractAddressR.get
        _       <- assertNotNone[F, Address]("call contract by send transaction", address)
        transaction = Request.Transaction(from = args.sender, data = args.data, to = address, opt = args.opt)
        promise <- cli.sendTransaction(transaction)
      } yield promise

      override def call(args: CallArgs): F[Deferred[F, Array[Byte]]] = for {
        address <- contractAddressR.get
        _       <- assertNotNone[F, Address]("call contract by eth call", address)
        callData = Request.Transaction(from = args.sender, data = args.data, to = address, opt = args.opt)
        promise <- cli.call(callData)
      } yield promise

      override def subscribeLogs(topic: Bytes32): F[SubscriptionResult[F, Log]] = for {
        addrOpt <- contractAddressR.get
        address <- assertNotNone[F, Address]("subscribe logs", addrOpt)
        result  <- cli.subscribeLogs(LogQuery.from(address, topic))
      } yield result
    }

    WebsocketClient.apply[F](endpoint).flatMap { wsClient =>
      Resource.liftF(newContract(wsClient))
    }
  }
}
