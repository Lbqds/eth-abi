package ethabi.protocol

import io.circe.Decoder
import io.circe.generic.auto._
import ethabi.protocol.Request.{Transaction => ReqTransaction, _}
import ethabi.protocol.Response._

import scala.concurrent.Future
import ethabi.types.Address
import ethabi.util.{Hash, Hex}

private [protocol] trait Service {
  import scala.concurrent.ExecutionContext.Implicits.global
  import Service._
  def allowSubscribe: Boolean
  def doRequest(req: Request): Future[Response]

  def clientVersion: Result[String] = doRequest(Request.clientVersion()).map(_.as[String])
  def sha3(data: Array[Byte]): Result[Hash] = doRequest(Request.sha3(data)).map(_.as[String].map(_.map(Hash.apply)))
  def netVersion: Result[String] = doRequest(Request.netVersion()).map(_.as[String])
  def netListening: Result[Boolean] = doRequest(Request.netListening()).map(_.as[Boolean])
  def peerCount: Result[Int] = doRequest(Request.netPeerCount()).map(_.as[String].map(_.map(Hex.hex2Int)))
  def protocolVersion: Result[String] = doRequest(Request.protocolVersion()).map(_.as[String])
  def syncing: Result[Either[Boolean, Response.Syncing]] = doRequest(Request.syncing()).map(_.decodeWith(Decoder[Boolean].either(Decoder[Response.Syncing])))
  def coinbase: Result[Address] = doRequest(Request.coinbase()).map(_.as[String].map(_.map(Address.apply)))
  def mining: Result[Boolean] = doRequest(Request.mining()).map(_.as[Boolean])
  def hashRate: Result[Long] = doRequest(Request.hashRate()).map(_.as[String].map(_.map(Hex.hex2Long)))
  def gasPrice: Result[BigInt] = doRequest(Request.gasPrice()).map(_.as[String].map(_.map(Hex.hex2BigInt)))
  def accounts: Result[Seq[Address]] = doRequest(Request.accounts()).map(_.as[Seq[String]].map(_.map(_.map(Address.apply))))
  def blockNumber: Result[Long] = doRequest(Request.blockNumber()).map(_.as[String].map(_.map(Hex.hex2Long)))
  def balance(address: Address, blockTag: BlockTag = Latest): Result[BigInt] =
    doRequest(Request.balance(address, blockTag)).map(_.as[String].map(_.map(Hex.hex2BigInt)))
  def balance(address: Address, height: Long): Result[BigInt] = balance(address, BlockNumber(height))
  def storageAt(address: Address, position: Int, blockTag: BlockTag = Latest): Result[Array[Byte]] =
    doRequest(Request.storageAt(address, position, blockTag)).map(_.as[String].map(_.map(Hex.hex2Bytes)))
  def storageAt(address: Address, position: Int, height: Long): Result[Array[Byte]] = storageAt(address, position, BlockNumber(height))
  def transactionCount(address: Address, blockTag: BlockTag = Latest): Result[Int] =
    doRequest(Request.transactionCount(address, blockTag)).map(_.as[String].map(_.map(Hex.hex2Int)))
  def transactionCount(address: Address, height: Long): Result[Int] = transactionCount(address, BlockNumber(height))
  def blockTransactionCountByHash(blockHash: Hash): Result[Int] =
    doRequest(Request.blockTransactionCountByHash(blockHash.toString)).map(_.as[String].map(_.map(Hex.hex2Int)))

  def sendTransaction(transaction: ReqTransaction): Result[Hash] = doRequest(Request.sendTransaction(transaction)).map(_.as[String].map(_.map(Hash.apply)))
  def transactionReceipt(txHash: Hash): Result[TransactionReceipt] = doRequest(Request.transactionReceipt(txHash)).map(_.as[TransactionReceipt])
  def call(callData: ReqTransaction, blockTag: BlockTag = Latest): Result[Array[Byte]] =
    doRequest(Request.call(callData, blockTag)).map(_.as[String].map(_.map(Hex.hex2Bytes)))
  def call(callData: ReqTransaction, height: Long): Result[Array[Byte]] = call(callData, BlockNumber(height))
  def logs(logQuery: LogQuery): Result[Seq[Log]] = doRequest(Request.logs(logQuery)).map(_.as[Seq[Log]])
}

object Service {
  type Result[T] = Future[Either[ResponseError, Option[T]]]
}