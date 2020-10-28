package ethabi
package protocol

import io.circe.Decoder
import ethabi.util.Hash
import ethabi.implicits._
import ethabi.types.Address
import Request._

trait Client[F[_], Ret[_[_] <: F[_], *]] {

  /**
   * send request to ethereum jsonrpc server
   *
   * @param request  refer to [[Request]]
   * @tparam R       result type for this request
   * @return         a computation with effect type F
   */
  def doRequest[R: Decoder](request: Request): F[Ret[F, R]]

  /**
   * @return ethereum client version, refer to https://eth.wiki/json-rpc/API#web3_clientversion
   */
  final def clientVersion: F[Ret[F, String]] = doRequest[String](Request.clientVersion())

  /**
   * compute sha3 hash for bytes, refer to https://eth.wiki/json-rpc/API#web3_sha3
   *
   * @param data   the data to convert into sha3 hash
   * @return       sha3 hash of giving data
   */
  final def sha3(data: Array[Byte]): F[Ret[F, Hash]] = doRequest[Hash](Request.sha3(data))

  /**
   * @return current network id, refer to https://eth.wiki/json-rpc/API#net_version
   */
  final def netVersion: F[Ret[F, String]] = doRequest[String](Request.netVersion())

  /**
   * @return number of peers currently connected to the client, refer to https://eth.wiki/json-rpc/API#net_peercount
   */
  final def peerCount: F[Ret[F, Int]] = doRequest[Int](Request.netPeerCount())

  /**
   * @return the current ethereum protocol version, refer to https://eth.wiki/json-rpc/API#eth_protocolversion
   */
  final def protocolVersion: F[Ret[F, String]] = doRequest[String](Request.protocolVersion())

  /**
   * @return [[Response.Syncing]] if is syncing, otherwise false, refer to https://eth.wiki/json-rpc/API#eth_syncing
   */
  final def syncing: F[Ret[F, Either[Boolean, Response.Syncing]]] =
    doRequest[Either[Boolean, Response.Syncing]](Request.syncing())(Decoder[Boolean].either(Decoder[Response.Syncing]))

  /**
   * @return client coinbase address, refer to https://eth.wiki/json-rpc/API#eth_coinbase
   */
  final def coinbase: F[Ret[F, Address]] = doRequest[Address](Request.coinbase())

  /**
   * @return if client is actively mining, refer to https://eth.wiki/json-rpc/API#eth_mining
   */
  final def mining: F[Ret[F, Boolean]] = doRequest[Boolean](Request.mining())

  /**
   * @return number of hash per second, refer to https://eth.wiki/json-rpc/API#eth_hashrate
   */
  final def hashrate: F[Ret[F, Int]] = doRequest[Int](Request.hashrate())

  /**
   * @return current gas price in wei, refer to https://eth.wiki/json-rpc/API#eth_gasPrice
   */
  final def gasPrice: F[Ret[F, BigInt]] = doRequest[BigInt](Request.gasPrice())

  /**
   * @return account list owned by client, refer to https://eth.wiki/json-rpc/API#eth_accounts
   */
  final def accounts: F[Ret[F, List[Address]]] = doRequest[List[Address]](Request.accounts())

  /**
   * @return current block number, refer to https://eth.wiki/json-rpc/API#eth_blockNumber
   */
  final def blockNumber: F[Ret[F, Long]] = doRequest[Long](Request.blockNumber())

  /**
   * @param address   account address
   * @param blockTag  refer to [[Request.BlockTag]]
   * @return  account balance in wei, refer to https://eth.wiki/json-rpc/API#eth_getBalance
   */
  final def getBalance(address: Address, blockTag: BlockTag = Latest): F[Ret[F, BigInt]] = doRequest[BigInt](Request.balance(address, blockTag))

  final def getBalance(address: Address, height: Long): F[Ret[F, BigInt]] = getBalance(address, BlockNumber(height))

  /**
   * @param address   address of the storage e.g. contract address
   * @param position  index of the position in the storage
   * @param blockTag  refer to [[Request.BlockTag]]
   * @return data in the storage position, refer to [[https://eth.wiki/json-rpc/API#eth_getStorageAt]]
   */
  final def getStorageAt(address: Address, position: Int, blockTag: BlockTag = Latest): F[Ret[F, Array[Byte]]] =
    doRequest[Array[Byte]](Request.storageAt(address, position, blockTag))

  final def getStorageAt(address: Address, position: Int, height: Long): F[Ret[F, Array[Byte]]] =
    getStorageAt(address, position, BlockNumber(height))

  /**
   * @param address  account address
   * @param blockTag refer to [[Request.BlockTag]]
   * @return number of transaction send by `address`, refer to https://eth.wiki/json-rpc/API#eth_getTransactionCount
   */
  final def getTransactionCount(address: Address, blockTag: BlockTag = Latest): F[Ret[F, Int]] =
    doRequest[Int](Request.transactionCount(address, blockTag))

  final def getTransactionCount(address: Address, height: Long): F[Ret[F, Int]] = getTransactionCount(address, BlockNumber(height))

  /**
   * @param hash block hash
   * @return number of transactions in this block, refer to https://eth.wiki/json-rpc/API#eth_getBlockTransactionCountByHash
   */
  final def getBlockTransactionCountByHash(hash: Hash): F[Ret[F, Int]] = doRequest[Int](Request.blockTransactionCountByHash(hash))

  /**
   * @param blockTag refer to [[Request.BlockTag]]
   * @return number of transactions in this block, refer to https://eth.wiki/json-rpc/API#eth_getBlockTransactionCountsByNumber
   */
  final def getBlockTransactionCountByNumber(blockTag: BlockTag = Latest): F[Ret[F, Int]] = doRequest[Int](Request.blockTransactionCountByNumber(blockTag))

  final def getBlockTransactionCountByNumber(height: Long): F[Ret[F, Int]] = getBlockTransactionCountByNumber(BlockNumber(height))

  /**
   * @param hash block hash
   * @return number of uncles in this block, refer to https://eth.wiki/json-rpc/API#eth_getUncleCountByBlockHash
   */
  final def getUncleCountByBlockHash(hash: Hash): F[Ret[F, Int]] = doRequest[Int](Request.uncleCountByHash(hash))

  /**
   * @param blockTag refer to [[Request.BlockTag]]
   * @return number of uncles in this block, refer to https://eth.wiki/json-rpc/API#eth_getUncleCountByBlockNumber
   */
  final def getUncleCountByBlockNumber(blockTag: BlockTag = Latest): F[Ret[F, Int]] = doRequest[Int](Request.uncleCountByNumber(blockTag))

  final def getUncleCountByBlockNumber(height: Long): F[Ret[F, Int]] = getUncleCountByBlockNumber(BlockNumber(height))

  /**
   * @param address  contract address
   * @param blockTag refer to [[Request.BlockTag]]
   * @return contract code, refer to https://eth.wiki/json-rpc/API#eth_getCode
   */
  final def getCode(address: Address, blockTag: BlockTag = Latest): F[Ret[F, Array[Byte]]] = doRequest[Array[Byte]](Request.code(address, blockTag))

  final def getCode(address: Address, height: Long): F[Ret[F, Array[Byte]]] = getCode(address, BlockNumber(height))

  /**
   * calculate signature for `data` with account private key
   *
   * @param address account address
   * @param data    data to sign
   * @return signature of data, refer to https://eth.wiki/json-rpc/API#eth_sign
   * @note   account MUST be unlocked
   */
  final def sign(address: Address, data: Array[Byte]): F[Ret[F, Array[Byte]]] = doRequest[Array[Byte]](Request.sign(address, data))

  /**
   * sign a unsigned transaction that can be submit to network with [[sendRawTransaction]]
   *
   * @param tx unsigned transaction
   * @return signed transaction, refer to https://eth.wiki/json-rpc/API#eth_signTransaction
   * @note   account MUST be unlocked
   */
  final def signTransaction(tx: Transaction): F[Ret[F, Array[Byte]]] = doRequest[Array[Byte]](Request.signTransaction(tx))

  /**
   * @param transaction unsigned transaction
   * @return transaction hash, refer to https://eth.wiki/json-rpc/API#eth_sendTransaction
   * @note   account MUST be unlocked
   */
  final def sendTransaction(transaction: Transaction): F[Ret[F, Hash]] = doRequest[Hash](Request.sendTransaction(transaction))

  /**
   * @param data signed transaction data
   * @return transaction hash, refer to https://eth.wiki/json-rpc/API#eth_sendRawTransaction
   */
  final def sendRawTransaction(data: Array[Byte]): F[Ret[F, Hash]] = doRequest[Hash](Request.sendRawTransaction(data))

  /**
   * execute contract method without create a transaction, therefore won't change world state
   *
   * @param callData same as [[Request.Transaction]] except for nonce
   * @param blockTag refer to [[Request.BlockTag]]
   * @return result of executed contract, https://eth.wiki/json-rpc/API#eth_call
   */
  final def call(callData: Transaction, blockTag: BlockTag = Latest): F[Ret[F, Array[Byte]]] = doRequest[Array[Byte]](Request.call(callData, blockTag))

  final def call(callData: Transaction, height: Long): F[Ret[F, Array[Byte]]] = call(callData, BlockNumber(height))

  /**
   * returns an estimate of how much gas is necessary to allow the transaction to complete
   *
   * @param callData same with Client.call
   * @param blockTag refer to [[Request.BlockTag]]
   * @return the estimate amount of gas, refer to https://eth.wiki/json-rpc/API#eth_estimateGas
   */
  final def estimateGas(callData: Transaction, blockTag: BlockTag = Latest): F[Ret[F, Long]] = doRequest[Long](Request.estimateGas(callData, blockTag))

  final def estimateGas(callData: Transaction, height: Long): F[Ret[F, Long]] = estimateGas(callData, BlockNumber(height))

  /**
   * return information about a block by hash
   *
   * @param hash block hash
   * @return [[Response.Block]], refer to https://eth.wiki/json-rpc/API#eth_getBlockByHash
   */
  final def getBlockByHash(hash: Hash): F[Ret[F, Option[Response.Block]]] =
    doRequest[Option[Response.Block]](Request.blockByHash(hash))

  /**
   * return information about a block by hash
   *
   * @param hash block hash
   * @return [[Response.BlockWithTransactions]], refer to https://eth.wiki/json-rpc/API#eth_getBlockByHash
   */
  final def getBlockByHashWithTransactions(hash: Hash): F[Ret[F, Option[Response.BlockWithTransactions]]] =
    doRequest[Option[Response.BlockWithTransactions]](Request.blockByHash(hash, detail = true))

  /**
   * return information about block by height
   *
   * @param blockTag refer to [[Request.BlockTag]]
   * @return [[Response.Block]], refer to https://eth.wiki/json-rpc/API#eth_getBlockByNumber
   */
  final def getBlockByNumber(blockTag: BlockTag = Latest): F[Ret[F, Option[Response.Block]]] =
    doRequest[Option[Response.Block]](Request.blockByNumber(blockTag))

  /**
   * return information about block by height
   *
   * @param height block height
   * @return [[Response.Block]], refer to https://eth.wiki/json-rpc/API#eth_getBlockByNumber
   */
  final def getBlockByNumber(height: Long): F[Ret[F, Option[Response.Block]]] =
    getBlockByNumber(BlockNumber(height))

  /**
   * return information about block by height
   *
   * @param blockTag refer to [[Request.BlockTag]]
   * @return [[Response.Block]], refer to https://eth.wiki/json-rpc/API#eth_getBlockByNumber
   */
  final def getBlockByNumberWithTransactions(blockTag: BlockTag = Latest): F[Ret[F, Option[Response.BlockWithTransactions]]] =
    doRequest[Option[Response.BlockWithTransactions]](Request.blockByNumber(blockTag, detail = true))

  /**
   * return information about block by height
   *
   * @param height  block height
   * @return [[Response.BlockWithTransactions]], refer to https://eth.wiki/json-rpc/API#eth_getBlockByNumber
   */
  final def getBlockByNumberWithTransactions(height: Long): F[Ret[F, Option[Response.BlockWithTransactions]]] =
    getBlockByNumberWithTransactions(BlockNumber(height))

  /**
   * returns the information about a transaction requested by transaction hash
   *
   * @param hash transaction hash
   * @return [[Response.Transaction]], refer to https://eth.wiki/json-rpc/API#eth_getTransactionByHash
   */
  final def getTransactionByHash(hash: Hash): F[Ret[F, Option[Response.Transaction]]] =
    doRequest(Request.transactionByHash(hash))

  /**
   * returns information about a transaction by block hash and transaction index position
   *
   * @param hash   block hash
   * @param index  transaction index in block
   * @return [[Response.Transaction]], refer to https://eth.wiki/json-rpc/API#eth_getTransactionBlockHashAndIndex
   */
  final def getTransactionByBlockHashAndIndex(hash: Hash, index: Int): F[Ret[F, Option[Response.Transaction]]] =
    doRequest(Request.transactionByBlockHashAndIndex(hash, index))

  /**
   * returns information about a transaction by block number and transaction index position
   *
   * @param blockTag refer to [[Request.BlockTag]]
   * @param index    transaction index in block
   * @return [[Response.Transaction]], refer to https://eth.wiki/json-rpc/API#eth_getTransactionByBlockNumberAndIndex
   */
  final def getTransactionByBlockNumberAndIndex(blockTag: BlockTag, index: Int): F[Ret[F, Option[Response.Transaction]]] =
    doRequest(Request.transactionByBlockNumberAndIndex(blockTag, index))

  /**
   * returns information about a transaction by block number and transaction index position
   *
   * @param height block number
   * @param index  transaction index in block
   * @return [[Response.Transaction]], refer to https://eth.wiki/json-rpc/API#eth_getTransactionByBlockNumberAndIndex
   */
  final def getTransactionByBlockNumberAndIndex(height: Long, index: Int): F[Ret[F, Option[Response.Transaction]]] =
    doRequest(Request.transactionByBlockNumberAndIndex(BlockNumber(height), index))

  /**
   * returns the receipt of a transaction by transaction hash
   *
   * @param txHash transaction hash
   * @return [[Response.TransactionReceipt]], refer to https://eth.wiki/json-rpc/API#eth_getTransactionReceipt
   * @note   receipt is not available for pending transaction
   */
  final def getTransactionReceipt(txHash: Hash): F[Ret[F, Option[Response.TransactionReceipt]]] =
    doRequest[Option[Response.TransactionReceipt]](Request.transactionReceipt(txHash))

  /**
   * returns information about a uncle of a block by hash and uncle index position
   *
   * @param hash  block hash
   * @param index uncle index in block
   * @return [[Response.Header]], refer to https://eth.wiki/json-rpc/API#eth_getUncleByBlockHashAndIndex
   */
  final def getUncleByBlockHashAndIndex(hash: Hash, index: Int): F[Ret[F, Option[Response.Header]]] =
    doRequest[Option[Response.Header]](Request.uncleByBlockHashAndIndex(hash, index))

  /**
   * returns information about a uncle of a block by hash and uncle index position
   *
   * @param blockTag refer to [[Request.BlockTag]]
   * @param index    uncle index in block
   * @return [[Response.Header]], refer to https://eth.wiki/json-rpc/API#eth_getUncleByBlockNumberAndIndex
   */
  final def getUncleByBlockNumberAndIndex(blockTag: BlockTag, index: Int): F[Ret[F, Option[Response.Header]]] =
    doRequest[Option[Response.Header]](Request.uncleByBlockNumberAndIndex(blockTag, index))

  /**
   * returns information about a uncle of a block by hash and uncle index position
   *
   * @param height block number
   * @param index  uncle index in block
   * @return [[Response.Header]], refer to https://eth.wiki/json-rpc/API#eth_getUncleByBlockNumberAndIndex
   */
  final def getUncleByBlockNumberAndIndex(height: Long, index: Int): F[Ret[F, Option[Response.Header]]] =
    getUncleByBlockNumberAndIndex(BlockNumber(height), index)

  /**
   * creates a filter object, based on filter options, to notify when the state changes
   *
   * @param filter refer to [[Request.LogFilter]]
   * @return filter id, which can be used with eth_getFilterChanges, refer to https://eth.wiki/json-rpc/API#eth_newFilter
   */
  final def newFilter(filter: LogFilter): F[Ret[F, Response.FilterId]] = doRequest[Response.FilterId](Request.newFilter(filter))

  /**
   * creates a filter in the node, to notify when a new block arrives
   *
   * @return filter id, which can be used with eth_getFilterChanges, refer to https://eth.wiki/json-rpc/API#eth_newBlockFilter
   */
  final def newBlockFilter: F[Ret[F, Response.FilterId]] = doRequest[Response.FilterId](Request.newBlockFilter())

  /**
   * creates a filter in the node, to notify when new pending transactions arrive
   *
   * @return filter id, which can be used with eth_getFilterChanges, refer to https://eth.wiki/json-rpc/API#eth_newPendingTransactionFilter
   */
  final def newPendingTransactionFilter: F[Ret[F, Response.FilterId]] = doRequest[Response.FilterId](Request.newPendingTransactionFilter())

  /**
   * uninstalls a filter with given id
   *
   * @param filterId filter id which return by [[newFilter]], [[newBlockFilter]] and [[newPendingTransactionFilter]]
   * @return true if succeed, false otherwise, refer to https://eth.wiki/json-rpc/API#eth_uninstallFilter
   */
  final def uninstallFilter(filterId: Response.FilterId): F[Ret[F, Boolean]] = doRequest[Boolean](Request.uninstallFilter(filterId))

  /**
   * refer to [[getFilterChanges]]
   * @note `filterId` MUST be returned by [[newBlockFilter]] or [[newPendingTransactionFilter]]
   */
  final def getFilterChangeHashes(filterId: Response.FilterId): F[Ret[F, List[Hash]]] =
    doRequest[List[Hash]](Request.filterChanges(filterId))

  /**
   * polling method for a filter, which returns an array of logs which occurred since last poll.
   *
   * @param filterId filter id which return by [[newFilter]], [[newBlockFilter]] and [[newPendingTransactionFilter]]
   * @return [[Response.Log]], refer to https://eth.wiki/json-rpc/API#eth_getFilterChanges
   */
  final def getFilterChanges(filterId: Response.FilterId): F[Ret[F, List[Response.Log]]] =
    doRequest[List[Response.Log]](Request.filterChanges(filterId))

  /**
   * refer to [[getFilterLogs]]
   * @note `fliterId` MUST be returned by [[newBlockFilter]] or [[newPendingTransactionFilter]]
   */
  final def getFilterLogHashes(filterId: Response.FilterId): F[Ret[F, List[Hash]]] =
    doRequest[List[Hash]](Request.filterLogs(filterId))

  /**
   * returns an array of all logs matching filter with given id
   *
   * @param filterId filter id which return by [[newFilter]], [[newBlockFilter]] and [[newPendingTransactionFilter]]
   * @return [[Response.Log]], refer to https://eth.wiki/json-rpc/API#eth_getFilterLogs
   */
  final def getFilterLogs(filterId: Response.FilterId): F[Ret[F, List[Response.Log]]] =
    doRequest[List[Response.Log]](Request.filterLogs(filterId))

  /**
   * returns an array of all logs matching a given filter object
   *
   * @param logQuery refer to [[Request.LogQuery]]
   * @return [[Response.Log]], refer to https://eth.wiki/json-rpc/API#eth_getLogs
   */
  final def getLogs(logQuery: LogQuery): F[Ret[F, List[Response.Log]]] =
    doRequest[List[Response.Log]](Request.logs(logQuery))

  /**
   * mining api, return [[Response.Work]], refer to https://eth.wiki/json-rpc/API#eth_getWork
   */
  final def getWork: F[Ret[F, Response.Work]] = doRequest[Response.Work](Request.work)

  /**
   * used for submitting a proof-of-work solution
   *
   * @param work pow work result from miner
   * @return true if succeed, false otherwise, refer to https://eth.wiki/json-rpc/API#eth_submitWork
   */
  final def submitWork(work: Work): F[Ret[F, Boolean]] = doRequest[Boolean](Request.submitWork(work))

  /**
   * used for submitting mining hashrate
   *
   * @param hashrate miner hashrate
   * @return true if succeed, false otherwise, refer to https://eth.wiki/json-rpc/API#eth_submitHashrate
   */
  final def submitHashrate(hashrate: Hashrate): F[Ret[F, Boolean]] =
    doRequest[Boolean](Request.submitHashrate(hashrate))
}
