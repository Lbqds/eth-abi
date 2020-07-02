package ethabi.protocol

import io.circe.Json
import io.circe.syntax._
import io.circe.Encoder
import io.circe.generic.JsonCodec
import ethabi.util._
import ethabi.types.Address
import ethabi.implicits._
import scala.collection.mutable
import ethabi.types.generated.Bytes32

@JsonCodec(encodeOnly = true)
final case class Request(jsonrpc: String, id: Id, params: Seq[Json], method: String) {
  def withId(id: Long): Request = copy(id = Id(id))
  def toJson: String = encode(this).toString
}

object Request {

  private val jsonrpcVersion = "2.0"

  def apply(method: String, params: Seq[Json] = Seq.empty[Json]): Request = {
    Request(jsonrpcVersion, Id.zero, params, method)
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
    def toJson: Json = encode(this)
    override def toString: String = toJson.spaces2
  }

  object Transaction {
    implicit val encoder: Encoder[Transaction] = (tx: Transaction) => {
      val opts = encode(tx.opt).asObject.get.toMap
      val json = mutable.Map.empty[String, Json]
      json("from") = encode(tx.from)
      if (tx.to.isDefined) json("to") = encode(tx.to.get)
      if (tx.data.nonEmpty) json("data") = encode(tx.data)
      (json ++ opts).asJson
    }

    def deployTransaction(callArgs: CallArgs): Transaction = {
      Transaction(from = callArgs.sender, to = None, data = callArgs.data, opt = callArgs.opt)
    }
  }

  /**
   * [] “anything”                                                                                           => List[Bytes32]
   * [A] “A in first position (and anything after)”                                                          => List[Bytes32]
   * [null, B] “anything in first position AND B in second position (and anything after)”                    => `List[Option[Bytes32]]`
   * [A, B] “A in first position AND B in second position (and anything after)”                              => List[Bytes32]
   * `[[A, B], [A, B]]` “(A OR B) in first position AND (A OR B) in second position (and anything after)”    => `List[List[Bytes32]]`
   *
   * therefore, the type of [[topics]] is `List[Either[Option[Bytes32], List[Bytes32]]]`
   */
  @JsonCodec(encodeOnly = true)
  final case class LogFilter(fromBlock: Option[BlockTag], toBlock: Option[BlockTag], addresses: List[Address], topics: List[Either[Option[Bytes32], List[Bytes32]]]) {
    def toJson: Json = encode(this)
    override def toString: String = toJson.spaces2
  }

  @JsonCodec(encodeOnly = true)
  final case class LogQuery(fromBlock: Option[BlockTag], toBlock: Option[BlockTag], addresses: List[Address], topics: List[Either[Option[Bytes32], List[Bytes32]]], blockHash: Option[Hash]) {
    def toJson: Json = encode(this)
    override def toString: String = toJson.spaces2
  }

  object LogQuery {
    def from(address: Address, topic: Bytes32): LogQuery =
      LogQuery(None, None, List(address), List(Left(Some(topic))), None)
  }

  final case class Work(nonce: Long, hash: Hash, mixHash: Hash)

  final case class Hashrate(rate: Bytes32, id: Bytes32)

  def sha3(data: Array[Byte]): Request =
    Request(method = "web3_sha3", params = Seq(encode(data)))

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
    Request(method = "eth_getBalance", params = Seq(encode(address), encode(blockTag)))

  def storageAt(address: Address, position: Int, blockTag: BlockTag): Request =
    Request(
      method = "eth_getStorageAt",
      params = Seq(encode(address), encode(position), encode(blockTag))
    )

  def transactionCount(address: Address, blockTag: BlockTag): Request =
    Request(method = "eth_getTransactionCount", params = Seq(encode(address), encode(blockTag)))

  def blockTransactionCountByHash(hash: Hash): Request =
    Request(method = "eth_getBlockTransactionCountByHash", params = Seq(encode(hash)))

  def blockTransactionCountByNumber(blockTag: BlockTag = Latest): Request =
    Request(method = "eth_getBlockTransactionCountByNumber", params = Seq(encode(blockTag)))

  def blockTransactionCountByNumber(height: Long): Request =
    blockTransactionCountByNumber(BlockNumber(height))

  def uncleCountByHash(hash: Hash): Request =
    Request(method = "eth_getUncleCountByBlockHash", params = Seq(encode(hash)))

  def uncleCountByNumber(blockTag: BlockTag = Latest): Request =
    Request(method = "eth_getUncleCountByBlockNumber", params = Seq(encode(blockTag)))

  def uncleCountByNumber(height: Long): Request =
    uncleCountByNumber(BlockNumber(height))

  def code(address: Address, blockTag: BlockTag = Latest): Request =
    Request(method = "eth_getCode", params = Seq(encode(address), encode(blockTag)))

  def code(address: Address, height: Long): Request =
    code(address, BlockNumber(height))

  def sign(address: Address, data: Array[Byte]): Request =
    Request(method = "eth_sign", params = Seq(encode(address), encode(data)))

  def signTransaction(tx: Transaction): Request =
    Request(method = "eth_signTransaction", params = Seq(tx.toJson))

  def sendTransaction(transaction: Transaction): Request =
    Request(method = "eth_sendTransaction", params = Seq(transaction.toJson))

  def sendRawTransaction(rawTx: Array[Byte]): Request =
    Request(method = "eth_sendRawTransaction", params = Seq(encode(rawTx)))

  def call(callData: Transaction, blockTag: BlockTag): Request =
    Request(method = "eth_call", params = Seq(callData.toJson, encode(blockTag)))

  def estimateGas(callData: Transaction, blockTag: BlockTag = Latest): Request =
    Request(method = "eth_estimateGas", params = Seq(callData.toJson, encode(blockTag)))

  def estimateGas(callData: Transaction, height: Long): Request = estimateGas(callData, BlockNumber(height))

  def blockByHash(hash: Hash, detail: Boolean = false): Request =
    Request(method = "eth_getBlockByHash", params = Seq(encode(hash), encode(detail)))

  def blockByNumber(blockTag: BlockTag = Latest, detail: Boolean = false): Request =
    Request(method = "eth_getBlockByNumber", params = Seq(encode(blockTag), encode(detail)))

  def transactionByHash(hash: Hash): Request = Request(method = "eth_getTransactionByHash", params = Seq(encode(hash)))

  def transactionByBlockHashAndIndex(hash: Hash, index: Int): Request =
    Request(
      method = "eth_getTransactionByBlockHashAndIndex",
      params = Seq(encode(hash), encode(index))
    )

  def transactionByBlockNumberAndIndex(blockTag: BlockTag = Latest, index: Int): Request =
    Request(
      method = "eth_getTransactionByBlockNumberAndIndex",
      params = Seq(encode(blockTag), encode(index))
    )

  def transactionReceipt(hash: Hash): Request = Request(method = "eth_getTransactionReceipt", params = Seq(encode(hash)))

  def uncleByBlockHashAndIndex(hash: Hash, index: Int): Request =
    Request(
      method = "eth_getUncleByBlockHashAndIndex",
      params = Seq(encode(hash), encode(index))
    )

  def uncleByBlockNumberAndIndex(blockTag: BlockTag = Latest, index: Int): Request =
    Request(
      method = "eth_getUncleByBlockNumberAndIndex",
      params = Seq(encode(blockTag), encode(index))
    )

  def newFilter(logFilter: LogFilter): Request = Request(method = "eth_newFilter", params = Seq(logFilter.toJson))
  def newBlockFilter(): Request = Request(method = "eth_newBlockFilter")
  def newPendingTransactionFilter(): Request = Request(method = "eth_newPendingTransactionFilter")
  def uninstallFilter(filterId: Response.FilterId): Request =
    Request(method = "eth_uninstallFilter", params = Seq(encode(filterId)))

  def filterChanges(filterId: Response.FilterId): Request =
    Request(method = "eth_getFilterChanges", params = Seq(encode(filterId)))

  def filterLogs(filterId: Response.FilterId): Request =
    Request(method = "eth_getFilterLogs", params = Seq(encode(filterId)))

  def logs(logQuery: LogQuery): Request = Request(method = "eth_getLogs", params = Seq(logQuery.toJson))

  def work: Request = Request(method = "eth_getWork")

  def submitWork(work: Work): Request =
    Request(
      method = "eth_submitWork",
      params = Seq(encode(work.nonce), encode(work.hash), encode(work.mixHash))
    )

  def submitHashrate(hashrate: Hashrate): Request =
    Request(
      method = "eth_submitHashrate",
      params = Seq(encode(hashrate.rate), encode(hashrate.id))
    )

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
