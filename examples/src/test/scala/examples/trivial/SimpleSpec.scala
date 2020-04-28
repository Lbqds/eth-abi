package examples.trivial

import akka.actor.ActorSystem
import akka.stream.Materializer
import org.scalatest.{Matchers, WordSpec}
import ethabi.protocol.Request.TransactionOpt
import ethabi.types.{Address, DynamicBytes}
import ethabi.types.generated.{TupleType4, Uint256}
import scala.util.{Failure, Success}

class SimpleSpec extends WordSpec with Matchers {
  "test simple contract" in {
    implicit val system = ActorSystem()
    implicit val materializer = Materializer(system)
    import system.dispatcher

    val sender = Address("0xe538b17ebf20efcf1c426cf1480e8a2a4b87cb1b")
    val contract = new Trivial("ws://127.0.0.1:8546")
    val opt = TransactionOpt(Some(BigInt(1000000)), Some(BigInt(10000)), None, None)
    contract.deploy(sender, opt)
    while (!contract.isDeployed) {
      Thread.sleep(1000)
    }
    println("deploy succeed, address is: " + contract.contractAddress)

    contract.subscribeTestEvent.runForeach(event => println(event.toString))

    val a = Uint256(BigInt(1000))
    val b = DynamicBytes(Array[Byte](0x01, 0x02, 0x03, 0x04))
    val c = Uint256(BigInt(3333))
    val d = DynamicBytes(Array[Byte](0x11, 0x22, 0x33, 0x44))
    val t = TupleType4[Uint256, DynamicBytes, Uint256, DynamicBytes](a, b, c, d)
    contract.trigger(t, sender, opt) onComplete {
      case Success(txHash) =>
        println("call trigger succeed: " + txHash)
        system.terminate()
      case Failure(exception) => println("call trigger failed: " + exception)
    }
  }
}
