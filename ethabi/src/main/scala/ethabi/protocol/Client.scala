package ethabi
package protocol

import cats.effect.concurrent.Deferred
import io.circe.Decoder
import ethabi.util.Hash
import ethabi.implicits._
import ethabi.types.Address
import Request._
import ethabi.protocol.Response.TransactionReceipt

trait Client[F[_]] {

  /**
   * ========= INTERNAL API =========
   *
   * @param request  refer to [[Request]]
   * @tparam R       result type for this request
   * @return         a computation with effect type [[F]]
   */
  protected def doRequest[R: Decoder](request: Request): F[Deferred[F, R]]

  /**
   * @return ethereum client version, refer to https://eth.wiki/json-rpc/API#web3_clientversion
   */
  final def clientVersion: F[Deferred[F, String]] = doRequest[String](Request.clientVersion())

  /**
   * compute sha3 hash for bytes, refer to https://eth.wiki/json-rpc/API#web3_sha3
   *
   * @param data   the data to convert into sha3 hash
   * @return       sha3 hash of giving data
   */
  final def sha3(data: Array[Byte]): F[Deferred[F, Hash]] = doRequest[Hash](Request.sha3(data))

  /**
   * @return current network id, refer to https://eth.wiki/json-rpc/API#net_version
   */
  final def netVersion: F[Deferred[F, String]] = doRequest[String](Request.netVersion())

  /**
   * @return number of peers currently connected to the client, refer to https://eth.wiki/json-rpc/API#net_peercount
   */
  final def peerCount: F[Deferred[F, Int]] = doRequest[Int](Request.netPeerCount())

  /**
   * @return the current ethereum protocol version, refer to https://eth.wiki/json-rpc/API#eth_protocolversion
   */
  final def protocolVersion: F[Deferred[F, String]] = doRequest[String](Request.protocolVersion())

  /**
   * @return [[Response.Syncing]] if is syncing, otherwise false, refer to https://eth.wiki/json-rpc/API#eth_syncing
   */
  final def syncing: F[Deferred[F, Either[Boolean, Response.Syncing]]] =
    doRequest[Either[Boolean, Response.Syncing]](Request.syncing())(Decoder[Boolean].either(Decoder[Response.Syncing]))

  /**
   * @return client coinbase address, refer to https://eth.wiki/json-rpc/API#eth_coinbase
   */
  final def coinbase: F[Deferred[F, Address]] = doRequest[Address](Request.coinbase())

  /**
   * @return if client is actively mining, refer to https://eth.wiki/json-rpc/API#eth_mining
   */
  final def mining: F[Deferred[F, Boolean]] = doRequest[Boolean](Request.mining())

  /**
   * @return number of hash per second, refer to https://eth.wiki/json-rpc/API#eth_hashrate
   */
  final def hashrate: F[Deferred[F, Int]] = doRequest[Int](Request.hashrate())

  /**
   * @return current gas price in wei, refer to https://eth.wiki/json-rpc/API#eth_gasPrice
   */
  final def gasPrice: F[Deferred[F, BigInt]] = doRequest[BigInt](Request.gasPrice())

  /**
   * @return account list owned by client, refer to https://eth.wiki/json-rpc/API#eth_accounts
   */
  final def accounts: F[Deferred[F, List[Address]]] = doRequest[List[Address]](Request.accounts())

  /**
   * @return current block number, refer to https://eth.wiki/json-rpc/API#eth_blockNumber
   */
  final def blockNumber: F[Deferred[F, Long]] = doRequest[Long](Request.blockNumber())

  /**
   * @param address   account address
   * @param blockTag  refer to [[BlockTag]]
   * @return  account balance in wei, refer to https://eth.wiki/json-rpc/API#eth_getBalance
   */
  final def getBalance(address: Address, blockTag: BlockTag = Latest): F[Deferred[F, BigInt]] = doRequest[BigInt](Request.balance(address, blockTag))

  final def getBalance(address: Address, height: Long): F[Deferred[F, BigInt]] = getBalance(address, BlockNumber(height))

  /**
   * @param address   address of the storage e.g. contract address
   * @param position  index of the position in the storage
   * @param blockTag  refer to [[BlockTag]]
   * @return data in the storage position, refer to https://eth.wiki/json-rpc/API#eth_getStorageAt
   */
  final def getStorageAt(address: Address, position: Int, blockTag: BlockTag = Latest): F[Deferred[F, Array[Byte]]] =
    doRequest[Array[Byte]](Request.storageAt(address, position, blockTag))

  final def getStorageAt(address: Address, position: Int, height: Long): F[Deferred[F, Array[Byte]]] =
    getStorageAt(address, position, BlockNumber(height))

  /**
   * @param address  account address
   * @param blockTag refer to [[BlockTag]]
   * @return number of transaction send by `address`, refer to https://eth.wiki/json-rpc/API#eth_getTransactionCount
   */
  final def getTransactionCount(address: Address, blockTag: BlockTag = Latest): F[Deferred[F, Int]] =
    doRequest[Int](Request.transactionCount(address, blockTag))

  final def getTransactionCount(address: Address, height: Long): F[Deferred[F, Int]] = getTransactionCount(address, BlockNumber(height))

  /**
   * @param hash block hash
   * @return number of transactions in this block, refer to https://eth.wiki/json-rpc/API#eth_getBlockTransactionCountByHash
   */
  final def getBlockTransactionCountByHash(hash: Hash): F[Deferred[F, Int]] = doRequest[Int](Request.blockTransactionCountByHash(hash))

  /**
   * @param blockTag refer to [[BlockTag]]
   * @return number of transactions in this block, refer to https://eth.wiki/json-rpc/API#eth_getBlockTransactionCountsByNumber
   */
  final def getBlockTransactionCountByNumber(blockTag: BlockTag = Latest): F[Deferred[F, Int]] = doRequest[Int](Request.blockTransactionCountByNumber(blockTag))

  final def getBlockTransactionCountByNumber(height: Long): F[Deferred[F, Int]] = getBlockTransactionCountByNumber(BlockNumber(height))

  /**
   * @param hash block hash
   * @return number of uncles in this block, refer to https://eth.wiki/json-rpc/API#eth_getUncleCountByBlockHash
   */
  final def getUncleCountByBlockHash(hash: Hash): F[Deferred[F, Int]] = doRequest[Int](Request.uncleCountByHash(hash))

  /**
   * @param blockTag refer to [[BlockTag]]
   * @return number of uncles in this block, refer to https://eth.wiki/json-rpc/API#eth_getUncleCountByBlockNumber
   */
  final def getUncleCountByBlockNumber(blockTag: BlockTag = Latest): F[Deferred[F, Int]] = doRequest[Int](Request.uncleCountByNumber(blockTag))

  final def getUncleCountByBlockNumber(height: Long): F[Deferred[F, Int]] = getUncleCountByBlockNumber(BlockNumber(height))

  /**
   * @param address  contract address
   * @param blockTag refer to [[BlockTag]]
   * @return contract code, refer to https://eth.wiki/json-rpc/API#eth_getCode
   */
  final def getCode(address: Address, blockTag: BlockTag = Latest): F[Deferred[F, Array[Byte]]] = doRequest[Array[Byte]](Request.code(address, blockTag))

  final def getCode(address: Address, height: Long): F[Deferred[F, Array[Byte]]] = getCode(address, BlockNumber(height))

  /**
   * calculate signature for `data` with account private key
   *
   * @param address account address
   * @param data    data to sign
   * @return signature of data, refer to https://eth.wiki/json-rpc/API#eth_sign
   * @note   account MUST be unlocked
   */
  final def sign(address: Address, data: Array[Byte]): F[Deferred[F, Array[Byte]]] = doRequest[Array[Byte]](Request.sign(address, data))

  /**
   * sign a unsigned transaction that can be submit to network with [[sendRawTransaction]]
   *
   * @param tx unsigned transaction
   * @return signed transaction, refer to https://eth.wiki/json-rpc/API#eth_signTransaction
   * @note   account MUST be unlocked
   */
  final def signTransaction(tx: Transaction): F[Deferred[F, Array[Byte]]] = doRequest[Array[Byte]](Request.signTransaction(tx))

  /**
   * @param transaction unsigned transaction
   * @return transaction hash, refer to https://eth.wiki/json-rpc/API#eth_sendTransaction
   * @note   account MUST be unlocked
   */
  final def sendTransaction(transaction: Transaction): F[Deferred[F, Hash]] = doRequest[Hash](Request.sendTransaction(transaction))

  /**
   * @param data signed transaction data
   * @return transaction hash, refer to https://eth.wiki/json-rpc/API#eth_sendRawTransaction
   */
  final def sendRawTransaction(data: Array[Byte]): F[Deferred[F, Hash]] = doRequest[Hash](Request.sendRawTransaction(data))

  /**
   * execute contract method without create a transaction, therefore won't change world state
   *
   * @param callData same as [[Transaction]] except for [[Transaction.opt.nonce]]
   * @param blockTag refer to [[BlockTag]]
   * @return result of executed contract, https://eth.wiki/json-rpc/API#eth_call
   */
  final def call(callData: Transaction, blockTag: BlockTag = Latest): F[Deferred[F, Array[Byte]]] = doRequest[Array[Byte]](Request.call(callData, blockTag))

  final def call(callData: Transaction, height: Long): F[Deferred[F, Array[Byte]]] = call(callData, BlockNumber(height))

  /**
   * returns an estimate of how much gas is necessary to allow the transaction to complete
   *
   * @param callData same with [[call]]
   * @param blockTag refer to [[BlockTag]]
   * @return the estimate amount of gas, refer to https://eth.wiki/json-rpc/API#eth_estimateGas
   */
  final def estimateGas(callData: Transaction, blockTag: BlockTag = Latest): F[Deferred[F, Long]] = doRequest[Long](Request.estimateGas(callData, blockTag))

  final def estimateGas(callData: Transaction, height: Long): F[Deferred[F, Long]] = estimateGas(callData, BlockNumber(height))

  /**
   * return information about a block by hash
   *
   * @param hash block hash
   * @return [[Response.Block]], refer to https://eth.wiki/json-rpc/API#eth_getBlockByHash
   */
  final def getBlockByHash(hash: Hash): F[Deferred[F, Response.Block]] = doRequest[Response.Block](Request.blockByHash(hash))

  /**
   * return information about a block by hash
   *
   * @param hash block hash
   * @return [[Response.BlockWithTransactions]], refer to https://eth.wiki/json-rpc/API#eth_getBlockByHash
   */
  final def getBlockByHashWithTransactions(hash: Hash): F[Deferred[F, Response.BlockWithTransactions]] =
    doRequest[Response.BlockWithTransactions](Request.blockByHash(hash, detail = true))

  /**
   * return information about block by height
   *
   * @param blockTag refer to [[BlockTag]]
   * @return [[Response.Block]], refer to https://eth.wiki/json-rpc/API#eth_getBlockByNumber
   */
  final def getBlockByNumber(blockTag: BlockTag = Latest): F[Deferred[F, Response.Block]] =
    doRequest[Response.Block](Request.blockByNumber(blockTag))

  /**
   * return information about block by height
   *
   * @param height block height
   * @return [[Response.Block]], refer to https://eth.wiki/json-rpc/API#eth_getBlockByNumber
   */
  final def getBlockByNumber(height: Long): F[Deferred[F, Response.Block]] = getBlockByNumber(BlockNumber(height))

  /**
   * return information about block by height
   *
   * @param blockTag refer to [[BlockTag]]
   * @return [[Response.Block]], refer to https://eth.wiki/json-rpc/API#eth_getBlockByNumber
   */
  final def getBlockByNumberWithTransactions(blockTag: BlockTag = Latest): F[Deferred[F, Response.BlockWithTransactions]] =
    doRequest[Response.BlockWithTransactions](Request.blockByNumber(blockTag, detail = true))

  /**
   * return information about block by height
   *
   * @param height  block height
   * @return [[Response.BlockWithTransactions]], refer to https://eth.wiki/json-rpc/API#eth_getBlockByNumber
   */
  final def getBlockByNumberWithTransactions(height: Long): F[Deferred[F, Response.BlockWithTransactions]] =
    getBlockByNumberWithTransactions(BlockNumber(height))

  final def getTransactionReceipt(txHash: Hash): F[Deferred[F, Option[TransactionReceipt]]] = doRequest[Option[TransactionReceipt]](Request.transactionReceipt(txHash))
}
