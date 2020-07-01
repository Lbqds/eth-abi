package ethabi.protocol

import io.circe.Json
import io.circe.syntax._
import io.circe.Encoder
import io.circe.generic.JsonCodec
import ethabi.util._
import ethabi.types.Address
import ethabi.implicits._
import scala.collection.mutable
import java.util.concurrent.atomic.AtomicLong

@JsonCodec(encodeOnly = true)
final case class Request(jsonrpc: String, id: Id, params: Seq[Json], method: String) {
  def withId(id: Long): Request = copy(id = Id(id))
  def toJson: String = Encoder[Request].apply(this).toString
}

object Request {

  private val jsonrpcVersion = "2.0"
  // TODO: request id per connection, use `copy`
  private val nextId: AtomicLong = new AtomicLong(1)

  def apply(method: String, params: Seq[Json] = Seq.empty[Json]): Request = {
    Request(jsonrpcVersion, Id(nextId.getAndIncrement()), params, method)
  }

  // call args for eth_sendTransaction & eth_call
  final case class CallArgs(data: Array[Byte], sender: Address, opt: TransactionOpt)

  sealed trait BlockTag

  object BlockTag {
    implicit val encoder: Encoder[BlockTag] = (v: BlockTag) => Json.fromString(v.toString)
  }

  case object Latest extends BlockTag {
    override def toString = "latest"
  }

  case object Earliest extends BlockTag {
    override def toString = "earliest"
  }

  case object Pending extends BlockTag {
    override def toString = "pending"
  }

  final case class BlockNumber(height: Long) extends BlockTag {
    override def toString: String = Hex.long2Hex(height, withPrefix = true)
  }

  @JsonCodec(encodeOnly = true)
  final case class TransactionOpt(gas: Option[BigInt], gasPrice: Option[BigInt], value: Option[BigInt], nonce: Option[Long]) {
    def withGas(gas: BigInt): TransactionOpt = copy(gas = Some(gas))
    def withGasPrice(gasPrice: BigInt): TransactionOpt = copy(gasPrice = Some(gasPrice))
    def withValue(value: BigInt): TransactionOpt = copy(value = Some(value))
    def withNonce(nonce: Long): TransactionOpt = copy(nonce = Some(nonce))
  }

  final case class Transaction(from: Address, to: Option[Address], data: Array[Byte], opt: TransactionOpt) {
    def toJson: Json = Encoder[Transaction].apply(this)
    override def toString: String = toJson.spaces2
  }

  object Transaction {
    implicit val encoder: Encoder[Transaction] = (tx: Transaction) => {
      val opts = Encoder[TransactionOpt].apply(tx.opt).asObject.get.toMap
      val json = mutable.Map.empty[String, Json]
      json("from") = Encoder[Address].apply(tx.from)
      if (tx.to.isDefined) json("to") = Encoder[Address].apply(tx.to.get)
      if (tx.data.nonEmpty) json("data") = Encoder[Array[Byte]].apply(tx.data)
      (json ++ opts).asJson
    }

    def deployTransaction(callArgs: CallArgs): Transaction = {
      Transaction(from = callArgs.sender, to = None, data = callArgs.data, opt = callArgs.opt)
    }
  }

  @JsonCodec(encodeOnly = true)
  final case class LogFilter(fromBlock: Option[BlockTag], toBlock: Option[BlockTag], addresses: List[Address], topics: Option[List[List[Hash]]]) {
    def toJson: Json = Encoder[LogFilter].apply(this)
    override def toString: String = toJson.spaces2
  }

  @JsonCodec(encodeOnly = true)
  final case class LogQuery(fromBlock: Option[BlockTag], toBlock: Option[BlockTag], addresses: List[Address], topics: Option[List[List[Hash]]], blockHash: Option[Hash]) {
    def toJson: Json = Encoder[LogQuery].apply(this)
    override def toString: String = toJson.spaces2
  }

  object LogQuery {
    def from(address: Address, topic: Hash): LogQuery =
      LogQuery(None, None, List(address), Some(List(List(topic))), None)
  }

  def sha3(data: Array[Byte]): Request =
    Request(method = "web3_sha3", params = Seq(Json.fromString(Hex.bytes2Hex(data, withPrefix = true))))

  def clientVersion(): Request = Request(method = "web3_clientVersion")
  def netVersion(): Request = Request(method = "net_version")
  def netListening(): Request = Request(method = "net_listening")
  def netPeerCount(): Request = Request(method = "net_peerCount")
  def protocolVersion(): Request = Request(method = "eth_protocolVersion")
  def syncing(): Request = Request(method = "eth_syncing")
  def coinbase(): Request = Request(method = "eth_coinbase")
  def mining(): Request = Request(method = "eth_mining")
  def hashrate(): Request = Request(method = "eth_hashrate")
  def gasPrice(): Request = Request(method = "eth_gasPrice")
  def accounts(): Request = Request(method = "eth_accounts")
  def blockNumber(): Request = Request(method = "eth_blockNumber")

  def balance(address: Address, blockTag: BlockTag): Request =
    Request(method = "eth_getBalance", params = Seq(address.toString, blockTag.toString).map(Json.fromString))

  def storageAt(address: Address, position: Int, blockTag: BlockTag): Request = {
    val positionHex = Hex.int2Hex(position, withPrefix = true)
    Request(method = "eth_getStorageAt", params = Seq(address.toString, positionHex, blockTag.toString).map(Json.fromString))
  }

  def transactionCount(address: Address, blockTag: BlockTag): Request =
    Request(method = "eth_getTransactionCount", params = Seq(address.toString, blockTag.toString).map(Json.fromString))

  def blockTransactionCountByHash(hash: Hash): Request =
    Request(method = "eth_getBlockTransactionCountByHash", params = Seq(Json.fromString(hash.toString)))

  def blockTransactionCountByNumber(blockTag: BlockTag = Latest): Request =
    Request(method = "eth_getBlockTransactionCountByNumber", params = Seq(Json.fromString(blockTag.toString)))

  def blockTransactionCountByNumber(height: Long): Request =
    blockTransactionCountByNumber(BlockNumber(height))

  def uncleCountByHash(hash: Hash): Request =
    Request(method = "eth_getUncleCountByBlockHash", params = Seq(Json.fromString(hash.toString)))

  def uncleCountByNumber(blockTag: BlockTag = Latest): Request =
    Request(method = "eth_getUncleCountByBlockNumber", params = Seq(Json.fromString(blockTag.toString)))

  def uncleCountByNumber(height: Long): Request =
    uncleCountByNumber(BlockNumber(height))

  def code(address: Address, blockTag: BlockTag = Latest): Request =
    Request(method = "eth_getCode", params = Seq(address.toString, blockTag.toString).map(Json.fromString))

  def code(address: Address, height: Long): Request =
    code(address, BlockNumber(height))

  def sign(address: Address, data: Array[Byte]): Request = {
    val dataHex = Hex.bytes2Hex(data, withPrefix = true)
    Request(method = "eth_sign", params = Seq(address.toString, dataHex).map(Json.fromString))
  }

  def signTransaction(tx: Transaction): Request =
    Request(method = "eth_signTransaction", params = Seq(tx.toJson))

  def sendTransaction(transaction: Transaction): Request =
    Request(method = "eth_sendTransaction", params = Seq(transaction.toJson))

  def sendRawTransaction(rawTx: Array[Byte]): Request = {
    val txHex = Hex.bytes2Hex(rawTx, withPrefix = true)
    Request(method = "eth_sendRawTransaction", params = Seq(Json.fromString(txHex)))
  }

  def call(callData: Transaction, blockTag: BlockTag): Request =
    Request(method = "eth_call", params = Seq(callData.toJson, Json.fromString(blockTag.toString)))

  def estimateGas(callData: Transaction, blockTag: BlockTag = Latest): Request =
    Request(method = "eth_estimateGas", params = Seq(callData.toJson, Json.fromString(blockTag.toString)))

  def estimateGas(callData: Transaction, height: Long): Request = estimateGas(callData, BlockNumber(height))

  def blockByHash(hash: Hash, detail: Boolean = false): Request =
    Request(method = "eth_getBlockByHash", params = Seq(Json.fromString(hash.toString), Json.fromBoolean(detail)))

  def blockByNumber(blockTag: BlockTag = Latest, detail: Boolean = false): Request =
    Request(method = "eth_getBlockByNumber", params = Seq(Json.fromString(blockTag.toString), Json.fromBoolean(detail)))

  def transactionByHash(hash: String): Request = Request(method = "eth_getTransactionByHash", params = Seq(Json.fromString(hash)))
  def transactionByHash(hash: Hash): Request = transactionByHash(hash.toString)

  def transactionByBlockHashAndIndex(hash: String, index: Int): Request = {
    val indexHex = Hex.int2Hex(index, withPrefix = true)
    Request(method = "eth_getTransactionByBlockHashAndIndex", params = Seq(hash, indexHex).map(Json.fromString))
  }

  def transactionByBlockHashAndIndex(hash: Hash, index: Int): Request = transactionByBlockHashAndIndex(hash.toString, index)
  def transactionByBlockNumberAndIndex(blockTag: BlockTag = Latest, index: Int): Request = {
    val indexHex = Hex.int2Hex(index, withPrefix = true)
    Request(method = "eth_getTransactionByBlockNumberAndIndex", params = Seq(blockTag.toString, indexHex).map(Json.fromString))
  }

  def transactionByBlockNumberAndIndex(height: Long, index: Int): Request = transactionByBlockNumberAndIndex(BlockNumber(height), index)
  def transactionReceipt(hash: String): Request = Request(method = "eth_getTransactionReceipt", params = Seq(Json.fromString(hash)))
  def transactionReceipt(hash: Hash): Request = transactionReceipt(hash.toString)

  def uncleByBlockHashAndIndex(hash: String, index: Int): Request = {
    val indexHex = Hex.int2Hex(index, withPrefix = true)
    Request(method = "eth_getUncleByBlockHashAndIndex", params = Seq(hash, indexHex).map(Json.fromString))
  }

  def uncleByBlockHashAndIndex(hash: Hash, index: Int): Request = uncleByBlockHashAndIndex(hash.toString, index)
  def uncleByBlockNumberAndIndex(blockTag: BlockTag = Latest, index: Int): Request = {
    val indexHex = Hex.int2Hex(index, withPrefix = true)
    Request(method = "eth_getUncleByBlockNumberAndIndex", params = Seq(blockTag.toString, indexHex).map(Json.fromString))
  }

  def uncleByBlockNumberAndIndex(height: Long, index: Int): Request =
    uncleByBlockNumberAndIndex(BlockNumber(height), index)

  def newFilter(logFilter: LogFilter): Request = Request(method = "eth_newFilter", params = Seq(logFilter.toJson))
  def newBlockFilter(): Request = Request(method = "eth_newBlockFilter")
  def newPendingTransactionFilter(): Request = Request(method = "eth_newPendingTransactionFilter")
  def uninstallFilter(filterId: Int): Request = {
    val filterIdHex = Hex.int2Hex(filterId, withPrefix = true)
    Request(method = "eth_uninstallFilter", params = Seq(Json.fromString(filterIdHex)))
  }

  def filterChanges(filterId: Int): Request = {
    val filterIdHex = Hex.int2Hex(filterId, withPrefix = true)
    Request(method = "eth_getFilterChanges", params = Seq(Json.fromString(filterIdHex)))
  }

  def filterLogs(filterId: Int): Request = {
    val filterIdHex = Hex.int2Hex(filterId, withPrefix = true)
    Request(method = "eth_getFilterLogs", params = Seq(Json.fromString(filterIdHex)))
  }

  def logs(logQuery: LogQuery): Request = Request(method = "eth_getLogs", params = Seq(logQuery.toJson))
  // TODO: more rpc api

  def subscribeNewHeader(): Request =
    Request(method = "eth_subscribe", params = Seq(Json.fromString("newHeads")))

  def subscribeLogs(logQuery: LogQuery): Request =
    Request(method = "eth_subscribe", params = Seq(Json.fromString("logs"), logQuery.toJson))

  def subscribeNewPendingTransactions(): Request =
    Request(method = "eth_subscribe", params = Seq(Json.fromString("newPendingTransactions")))

  def subscribeSyncStatus(): Request = Request(method = "eth_subscribe", params = Seq(Json.fromString("syncing")))

  def unsubscribe(subscriptionId: String): Request =
    Request(method = "eth_unsubscribe", params = Seq(Json.fromString(subscriptionId)))
}
