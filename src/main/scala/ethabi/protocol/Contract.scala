package ethabi.protocol

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import ethabi.types.{Address, SolType, TupleType, TypeInfo}
import ethabi.util.{Hash, Hex}
import ethabi.protocol.ws.Client
import ethabi.protocol.Request._
import ethabi.protocol.Response.Log
import ethabi.types.generated.Bytes32

class Contract(val endpoint: String) {
  private implicit val system = ActorSystem()
  private implicit val materializer = ActorMaterializer()
  private var contractCreator: Option[Address] = None
  private var contractAddress: Option[Address] = None
  private val client = Client(endpoint)

  implicit def dispatcher = system.dispatcher
  def address: Option[Address] = contractAddress
  def creator: Option[Address] = contractCreator
  def load(address: Address): Unit = contractAddress = Some(address)
  def isDeployed: Boolean = contractAddress.isDefined

  def sendTransaction(data: Array[Byte], sender: Address, opt: TransactionOpt): Future[Hash] = {
    if (contractAddress.isEmpty) throw new RuntimeException("contract address is empty when call contract method")
    val transaction = Transaction(sender, contractAddress, data, opt)
    client.sendTransaction(transaction).map {
      case Left(responseError) => throw new RuntimeException(s"send transaction failed, $responseError")
      case Right(None) => throw new RuntimeException(s"no transaction hash after send succeed")
      case Right(Some(txHash)) => txHash
    }
  }

  def call(data: Array[Byte], sender: Address, opt: TransactionOpt): Future[Array[Byte]] = {
    if (contractAddress.isEmpty) throw new RuntimeException("contract address is empty when call contract method")
    val callData = Transaction(sender, contractAddress, data, opt)
    client.call(callData).map {
      case Left(responseError) => throw new RuntimeException(s"call contract failed, $responseError")
      case Right(None) => Array.empty[Byte]
      case Right(Some(value)) => value
    }
  }

  // data include all constructor arguments
  def deploy(data: Array[Byte], sender: Address, opt: TransactionOpt): Unit = {
    contractCreator = Some(sender)
    val transaction = Transaction(sender, None, data, opt)
    client.sendTransaction(transaction) onComplete {
      case Success(response) => response match {
        case Right(Some(txHash)) => afterDeploy(txHash)
        case Left(responseError) => throw new RuntimeException(s"deploy contract failed: $responseError")
        case Right(None) => throw new RuntimeException("deploy contract failed, no tx hash return")
      }
      case Failure(exception) => throw new RuntimeException(s"deploy contract failed: $exception")
    }
  }

  private def afterDeploy(txHash: Hash): Unit =
    client.transactionReceipt(txHash) onComplete {
      case Success(response) => response match {
        case Left(responseError) => throw new RuntimeException(s"deploy contract failed: $responseError")
        case Right(None) => system.scheduler.scheduleOnce(2 seconds, () => afterDeploy(txHash))
        case Right(Some(receipt)) =>
          assert(receipt.contractAddress.isDefined)
          // call `get` explicitly
          contractAddress = Some(Address(receipt.contractAddress.get))
      }
      case Failure(exception) => throw exception
    }

  def subscribeLogs(logQuery: LogQuery) = client.subscribeLogs(logQuery)
}

object Contract {
  def apply(endpoint: String) = new Contract(endpoint)
}

case class EventValue(indexedValues: Seq[SolType], nonIndexedValues: Seq[SolType]) {
  override def toString: String = {
    s"""
       |{
       |  indexedValues: ${indexedValues.mkString("[", ", ", "]")},
       |  nonIndexedValues: ${nonIndexedValues.mkString("[", ", ", "]")}
       |}
    """.stripMargin
  }
}

object EventValue {
  def decodeEvent(typeInfos: Seq[TypeInfo[_ <: SolType]], log: Log): EventValue = {
    val topics = log.topics.slice(1, log.topics.length).map(Hex.hex2Bytes)
    val data = Hex.hex2Bytes(log.data)
    val indexedValues = topics.zip(typeInfos).map {
      case (bytes, typeInfo) =>
        if (typeInfo.isStatic) typeInfo.decode(bytes, 0)._1
        else Bytes32(bytes)
    }
    val nonIndexedTypeInfo = typeInfos.slice(topics.length, typeInfos.length).headOption
    val nonIndexedValues = nonIndexedTypeInfo.map(_.decode(data, 0)._1.asInstanceOf[TupleType].toList)
    EventValue(indexedValues, nonIndexedValues.getOrElse(Seq.empty))
  }
}
