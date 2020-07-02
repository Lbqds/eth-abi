package examples
package kvstore

import cats.effect._
import ethabi.types._
import ethabi.types.generated._
import ethabi.protocol._
import ethabi.protocol.Request._
import ethabi.protocol.Response._
import retry.RetryPolicies
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

object Main extends IOApp {

  /*
  private val _ = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool(
    new NamedThreadFactory("ec1", true)
  ))
   */

  private val _ = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool(
    new NamedThreadFactory("ec2", true)
  ))

  // from ZIO
  class NamedThreadFactory(name: String, daemon: Boolean) extends ThreadFactory {

    private val parentGroup = Option(System.getSecurityManager).fold(Thread.currentThread().getThreadGroup)(_.getThreadGroup)

    private val threadGroup = new ThreadGroup(parentGroup, name)
    private val threadCount = new AtomicInteger(1)
    private val threadHash = Integer.toUnsignedString(this.hashCode())

    override def newThread(r: Runnable): Thread = {
      val newThreadNumber = threadCount.getAndIncrement()

      val thread = new Thread(threadGroup, r)
      thread.setName(s"$name-$newThreadNumber-$threadHash")
      thread.setDaemon(daemon)

      thread
    }
  }

  private def log(str: String): IO[Unit] = IO.delay(println(s"${Thread.currentThread.getName}, $str"))

  override def run(args: List[String]): IO[ExitCode] = {
    val sender = Address("60f7947aef8bbc9bc314a9b8db8096099345fba3")
    val transactionOpt = TransactionOpt(Some(400000), Some(1000), None, None)
    val retryPolicy = retry.RetryPolicies.limitRetries[IO](5).join(RetryPolicies.constantDelay[IO](5 seconds))
    KVStore[IO]("ws://127.0.0.1:8546").use { kvStore =>
      val task = for {
        client     <- kvStore.client
        peerCount  <- client.peerCount.flatMap(_.get)
        _          <- log(s"peer count: $peerCount")
        cliVersion <- client.clientVersion.flatMap(_.get)
        _          <- log(s"client version: $cliVersion")
        work       <- client.getWork.flatMap(_.get)
        _          <- log(s"work response: $work")
        protocolV  <- client.protocolVersion.flatMap(_.get)
        _          <- log(s"protocol version: $protocolV")
        coinbase   <- client.coinbase.flatMap(_.get)
        _          <- log(s"coinbase address: $coinbase")
        syncStatus <- client.syncing.flatMap(_.get)
        _          <- log(s"sync status: $syncStatus")
        deployHash <- kvStore.deploy(sender, transactionOpt)
        address    <- retryUntil[IO, Option[Address]]("wait contract deployed", retryPolicy, kvStore.address, _.isDefined).map(_.get)
        _          <- log(s"contract deploy succeed, address: $address")
        contractTx <- deployHash.get.flatMap(client.getTransactionByHash).flatMap(_.get)
        _          <- log(s"contract deploy tx: ${contractTx.get}")
        result     <- kvStore.subscribeRecord
        _          <- log(s"subscription id: ${result.id}")
        fiber      <- result.stream.forall { event =>
          println(event)
          true
        }.compile.drain.start
        txHash     <- kvStore.set(Uint16(12), DynamicBytes.from("0x010203040506070809"), sender, transactionOpt).flatMap(_.get)
        receipt    <- retryUntil[IO, Option[TransactionReceipt]](
          "wait tx receipt",
          retryPolicy,
          client.getTransactionReceipt(txHash).flatMap(_.get),
          _.isDefined
        ).map(_.get)
        _          <- log(s"tx receipt: $receipt")
        result     <- kvStore.get(Uint16(12), sender, transactionOpt)    // NOTE: will block at here
        _          <- log(s"key: 12, value: $result")
        _          <- fiber.cancel
        _          <- log("quit now")
      } yield ()
      task.handleErrorWith(exp => IO.delay(exp.printStackTrace())) *> IO.delay(ExitCode.Success)
    }
  }
}
