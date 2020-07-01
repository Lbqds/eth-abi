package ethabi
package protocol

import cats.effect.concurrent.Deferred
import io.circe.Decoder
import io.circe.generic.auto._
import ethabi.util.Hash
import ethabi.implicits._
import Request._
import ethabi.protocol.Response.TransactionReceipt

trait Client[F[_]] {

  /**
   * INTERNAL API
   * @param request  refer to [[Request]]
   * @tparam R       result type for this request
   * @return         a computation with effect type [[F]]
   */
  protected def doRequest[R: Decoder](request: Request): F[Deferred[F, Option[R]]]

  /**
   * get current ethereum client version, refer to https://eth.wiki/json-rpc/API#web3_clientversion
   *
   * @return ethereum client version e.g. "Geth/test-chain/v1.9.16-unstable-07aeb3f4-20200619/darwin-amd64/go1.14.2"
   */
  final def clientVersion: F[Deferred[F, Option[String]]] = doRequest[String](Request.clientVersion())

  /**
   * compute sha3 hash for bytes, refer to https://eth.wiki/json-rpc/API#web3_sha3
   *
   * @param data   the data to convert into sha3 hash
   * @return       sha3 hash of giving data
   */
  final def sha3(data: Array[Byte]): F[Deferred[F, Option[Hash]]] = doRequest[Hash](Request.sha3(data))

  /**
   * get current network id, refer to https://eth.wiki/json-rpc/API#net_version
   *
   * @return the current network id e.g. "1" for ethereum mainnet
   */
  final def netVersion: F[Deferred[F, Option[String]]] = doRequest[String](Request.netVersion())

  final def sendTransaction(transaction: Transaction): F[Deferred[F, Option[Hash]]] = doRequest[Hash](Request.sendTransaction(transaction))

  final def getTransactionReceipt(txHash: Hash): F[Deferred[F, Option[TransactionReceipt]]] = doRequest[TransactionReceipt](Request.transactionReceipt(txHash))

  final def call(callData: Transaction, blockTag: BlockTag = Latest): F[Deferred[F, Option[Array[Byte]]]] = doRequest[Array[Byte]](Request.call(callData, blockTag))

  final def call(callData: Transaction, height: Long): F[Deferred[F, Option[Array[Byte]]]] = call(callData, BlockNumber(height))
}
