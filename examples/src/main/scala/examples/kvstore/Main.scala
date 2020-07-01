package examples
package kvstore

import cats.effect._
import ethabi.types._
import ethabi.types.generated._
import ethabi.protocol._
import ethabi.protocol.Request._
import ethabi.protocol.Response._
import ethabi.util._
import retry.RetryPolicies
import scala.concurrent.duration._

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    val sender = Address("60f7947aef8bbc9bc314a9b8db8096099345fba3")
    val transactionOpt = TransactionOpt(Some(400000), Some(1000), None, None)
    val retryPolicy = retry.RetryPolicies.limitRetries[IO](5).join(RetryPolicies.constantDelay[IO](5 seconds))
    KVStore[IO]("ws://127.0.0.1:8546").use { kvStore =>
      for {
        _          <- kvStore.deploy(sender, transactionOpt)
        client     <- kvStore.client
        address    <- retryUntil[IO, Option[Address]]("wait contract deployed", retryPolicy, kvStore.address, _.isDefined).map(_.get)
        _          <- IO.delay(println(s"contract deploy succeed, address: $address"))
        result     <- kvStore.subscribeRecord
        _          <- IO.delay(println(s"subscription id: ${result.id}"))
        fiber      <- result.stream.forall { event =>
          println(event)
          true
        }.compile.drain.start
        _          <- IO.delay("")
        txP        <- kvStore.set(Uint16(12), DynamicBytes.from("0x010203040506070809"), sender, transactionOpt)
        txHash     <- retryUntil[IO, Option[Hash]]("wait tx hash", retryPolicy, txP.get, _.isDefined).map(_.get)
        receipt    <- retryUntil[IO, Option[TransactionReceipt]](
          "wait tx receipt",
          retryPolicy,
          client.getTransactionReceipt(txHash).flatMap(_.get),
          _.isDefined
        ).map(_.get)
        _          <- IO.delay(println(s"tx receipt: $receipt"))
        result     <- kvStore.get(Uint16(12), sender, transactionOpt)    // NOTE: will block at here
        _          <- IO.delay(println(s"key: 12, value: $result"))
        _          <- fiber.cancel
        _          <- IO.delay(println("quit now"))
      } yield ExitCode.Success
    }
  }
}
