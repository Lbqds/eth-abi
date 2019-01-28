package ethabi.protocol

import io.circe.Json
import io.circe.syntax._
import ethabi.util.{Hex, Hash}
import ethabi.types.Address
import scala.collection.mutable
import java.util.concurrent.atomic.AtomicLong

case class Request(jsonrpc: String, id: Long, params: Seq[Json], method: String) {
  def withId(id: Long): Request = copy(id = id)
}

object Request {
  private val jsonrpcVersion = "2.0"
  private val nextId: AtomicLong = new AtomicLong(1)

  def apply(method: String, params: Seq[Json] = Seq.empty[Json]): Request = {
    Request(jsonrpcVersion, nextId.getAndIncrement(), params, method)
  }

  sealed trait BlockTag
  case object Latest extends BlockTag {
    override def toString = "latest"
  }
  case object Earliest extends BlockTag {
    override def toString = "earliest"
  }
  case object Pending extends BlockTag {
    override def toString = "pending"
  }
  case class BlockNumber(height: Long) extends BlockTag {
    override def toString = Hex.long2Hex(height, withPrefix = true)
  }

  case class TransactionOpt(gas: Option[BigInt], gasPrice: Option[BigInt], value: Option[BigInt], nonce: Option[Long]) {
    def withGas(gas: BigInt): TransactionOpt = copy(gas = Some(gas))
    def withGasPrice(gasPrice: BigInt): TransactionOpt = copy(gasPrice = Some(gasPrice))
    def withValue(value: BigInt): TransactionOpt = copy(value = Some(value))
    def withNonce(nonce: Long): TransactionOpt = copy(nonce = Some(nonce))
  }

  case class Transaction(from: Address, to: Option[Address], data: Array[Byte], opt: TransactionOpt) {
    def toJson: Json = {
      val json = mutable.Map.empty[String, String]
      json("from") = from.toString
      if (to.isDefined) json("to") = to.get.toString
      if (!data.isEmpty) json("data") = Hex.bytes2Hex(data, withPrefix = true)
      if (opt.gas.isDefined) json("gas") = Hex.bigInt2Hex(opt.gas.get, withPrefix = true)
      if (opt.gasPrice.isDefined) json("gasPrice") = Hex.bigInt2Hex(opt.gasPrice.get, withPrefix = true)
      if (opt.value.isDefined) json("value") = Hex.bigInt2Hex(opt.value.get, withPrefix = true)
      if (opt.nonce.isDefined) json("nonce") = Hex.bigInt2Hex(opt.nonce.get, withPrefix = true)
      json.asJson
    }
    override def toString = toJson.spaces2
  }

  case class LogFilter(fromBlock: Option[BlockTag], toBlock: Option[BlockTag], addresses: Seq[Address], topics: Option[Array[Array[Hash]]]) {
    def toJson: Json = {
      val json = mutable.Map.empty[String, Json]
      if (fromBlock.isDefined) json("fromBlock") = Json.fromString(fromBlock.get.toString)
      if (toBlock.isDefined) json("toBlock") = Json.fromString(toBlock.get.toString)
      json("address") = Json.fromValues(addresses.map(addr => Json.fromString(addr.toString)))
      if (topics.isDefined) json("topics") = Json.fromValues(topics.get.map(arr => Json.fromValues(arr.map(hash => Json.fromString(hash.toString)))))
      json.asJson
    }
    override def toString = toJson.spaces2
  }

  case class LogQuery(fromBlock: Option[BlockTag], toBlock: Option[BlockTag], addresses: Seq[Address], topics: Option[Array[Array[Hash]]], blockHash: Option[Hash]) {
    def toJson: Json = {
      val json = mutable.Map.empty[String, Json]
      if (fromBlock.isDefined) json("fromBlock") = Json.fromString(fromBlock.get.toString)
      if (toBlock.isDefined) json("toBlock") = Json.fromString(toBlock.get.toString)
      json("address") = Json.fromValues(addresses.map(addr => Json.fromString(addr.toString)))
      if (topics.isDefined) json("topics") = Json.fromValues(topics.get.map(arr => Json.fromValues(arr.map(hash => Json.fromString(hash.toString)))))
      if (blockHash.isDefined) json("blockHash") = Json.fromString(blockHash.get.toString)
      json.asJson
    }
    override def toString = toJson.spaces2
  }

  object LogQuery {
    def from(address: Address, topic: Hash): LogQuery =
      LogQuery(None, None, Seq(address), Some(Array(Array(topic))), None)
  }

  def clientVersion(): Request = Request(method = "web3_clientVersion")
  def sha3(data: Array[Byte]): Request = {
    Request(method = "web3_sha3", params = Seq(Json.fromString(Hex.bytes2Hex(data, withPrefix = true))))
  }
  def netVersion(): Request = Request(method = "net_version")
  def netListening(): Request = Request(method = "net_listening")
  def netPeerCount(): Request = Request(method = "net_peerCount")
  def protocolVersion(): Request = Request(method = "eth_protocolVersion")
  def syncing(): Request = Request(method = "eth_syncing")
  def coinbase(): Request = Request(method = "eth_coinbase")
  def mining(): Request = Request(method = "eth_mining")
  def hashRate(): Request = Request(method = "eth_hashrate")
  def gasPrice(): Request = Request(method = "eth_gasPrice")
  def accounts(): Request = Request(method = "eth_accounts")
  def blockNumber(): Request = Request(method = "eth_blockNumber")
  def balance(address: Address, blockTag: BlockTag): Request = {
    Request(method = "eth_getBalance", params = Seq(address.toString, blockTag.toString).map(Json.fromString))
  }
  def storageAt(address: Address, position: Int, blockTag: BlockTag): Request = {
    val positionHex = Hex.int2Hex(position, withPrefix = true)
    Request(method = "eth_getStorageAt", params = Seq(address.toString, positionHex, blockTag.toString).map(Json.fromString))
  }
  def transactionCount(address: Address, blockTag: BlockTag): Request = {
    Request(method = "eth_getTransactionCount", params = Seq(address.toString, blockTag.toString).map(Json.fromString))
  }
  def blockTransactionCountByHash(blockHash: String): Request = {
    Request(method = "eth_getBlockTransactionCountByHash", params = Seq(Json.fromString(blockHash)))
  }
  def blockTransactionCountByNumber(blockTag: BlockTag = Latest): Request = {
    Request(method = "eth_getBlockTransactionCountByNumber", params = Seq(Json.fromString(blockTag.toString)))
  }
  def blockTransactionCountByNumber(height: Long): Request = {
    blockTransactionCountByNumber(BlockNumber(height))
  }
  def uncleCountByHash(blockHash: String): Request = {
    Request(method = "eth_getUncleCountByBlockHash", params = Seq(Json.fromString(blockHash)))
  }
  def uncleCountByNumber(blockTag: BlockTag = Latest): Request = {
    Request(method = "eth_getUncleCountByBlockNumber", params = Seq(Json.fromString(blockTag.toString)))
  }
  def uncleCountByNumber(height: Long): Request = {
    uncleCountByNumber(BlockNumber(height))
  }
  def code(address: Address, blockTag: BlockTag = Latest): Request = {
    Request(method = "eth_getCode", params = Seq(address.toString, blockTag.toString).map(Json.fromString))
  }
  def code(address: Address, height: Long): Request = {
    code(address, BlockNumber(height))
  }
  def sign(address: Address, data: String): Request = {
    Request(method = "eth_sign", params = Seq(address.toString, data).map(Json.fromString))
  }
  def sing(address: Address, data: Array[Byte]): Request = {
    val dataHex = Hex.bytes2Hex(data, withPrefix = true)
    Request(method = "eth_sign", params = Seq(address.toString, dataHex).map(Json.fromString))
  }
  def sendTransaction(transaction: Transaction): Request = {
    Request(method = "eth_sendTransaction", params = Seq(transaction.toJson))
  }
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
  def blockByNumber(blockTag: BlockTag = Latest, detail: Boolean = false): Request = {
    Request(method = "eth_getBlockByNumber", params = Seq(Json.fromString(blockTag.toString), Json.fromBoolean(detail)))
  }
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
  def uncleByBlockNumberAndIndex(height: Long, index: Int): Request = {
    uncleByBlockNumberAndIndex(BlockNumber(height), index)
  }
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

  def subscribeNewHeader(includeTransactions: Boolean = false): Request =
    Request(method = "eth_subscribe", params = Seq(Json.fromString("newHeads"), Map("includeTransactions" -> includeTransactions).asJson))
  def subscribeLogs(logQuery: LogQuery): Request =
    Request(method = "eth_subscribe", params = Seq(Json.fromString("logs"), logQuery.toJson))
  def subscribeNewPendingTransactions(): Request =
    Request(method = "eth_subscribe", params = Seq(Json.fromString("newPendingTransactions")))
  def subscribeSyncStatus(): Request = Request(method = "eth_subscribe", params = Seq(Json.fromString("syncing")))
  def unsubscribe(subscriptionId: String): Request =
    Request(method = "eth_unsubscribe", params = Seq(Json.fromString(subscriptionId)))
}
