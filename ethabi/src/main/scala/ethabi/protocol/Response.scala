package ethabi.protocol

import io.circe._
import io.circe.generic.JsonCodec
import cats.ApplicativeError
import cats.Applicative
import cats.effect.Sync
import Response.ResponseError
import ethabi.implicits._
import ethabi.util._
import ethabi.types.Address
import ethabi.types.generated.Bytes32

@JsonCodec(decodeOnly = true)
final case class Response(jsonrpc: String, id: Id, result: Json, error: Option[ResponseError]) {
  private val response: Either[ResponseError, Json] = if (error.isDefined) Left(error.get) else Right(result)

  def convertTo[T: Decoder, F[_]: Sync]: F[T] = response match {
    case Right(json) =>
      json.as[T].fold(ApplicativeError[F, Throwable].raiseError, x => Applicative[F].pure(x))
    case Left(e) => ApplicativeError[F, Throwable].raiseError(new RuntimeException(e.message))
  }
}

object Response {
  type FilterId = BigInt

  @JsonCodec(decodeOnly = true)
  final case class ResponseError(code: Id, message: String, data: Option[Json])

  @JsonCodec(decodeOnly = true)
  final case class Syncing(startingBlock: Long, currentBlock: Long, highestBlock: Long)

  // different with `Syncing`, all block number is Long rather than hex-string, refer to https://github.com/ethereum/go-ethereum/wiki/RPC-PUB-SUB#syncing
  @JsonCodec(decodeOnly = true)
  final case class SyncProgress(startingBlock: Long, currentBlock: Long, highestBlock: Long, pulledStates: Long, knownStates: Long)

  @JsonCodec(decodeOnly = true)
  final case class SyncStatus(syncing: Boolean, status: Option[SyncProgress])

  // `logsBloom` and `extraData` keep hex string format
  // TODO: `mixHash` is Option???
  @JsonCodec(decodeOnly = true) final case class Header(
    parentHash: Hash, sha3Uncles: Hash, miner: Address, stateRoot: Hash, transactionsRoot: Hash, size: Int,
    receiptsRoot: Hash, logsBloom: String, difficulty: BigInt, number: Long, totalDifficulty: BigInt,
    gasLimit: Long, gasUsed: Long, timestamp: Long, extraData: String, mixHash: Hash, nonce: Long, hash: Hash
  )

  final case class Block(header: Header, transactionsHash: List[Hash], uncles: Option[List[Hash]])

  object Block {
    implicit val decoder: Decoder[Block] = (c: HCursor) => {
      for {
        header  <- Decoder[Header].apply(c)
        txsHash <- c.downField("transactions").as[List[Hash]]
        uncles  <- c.downField("uncles").as[Option[List[Hash]]]
      } yield Block(header, txsHash, uncles)
    }
  }

  final case class BlockWithTransactions(header: Header, transactions: List[Transaction], uncles: Option[List[Hash]])

  object BlockWithTransactions {
    implicit val decoder: Decoder[BlockWithTransactions] = (c: HCursor) => {
      for {
        header <- Decoder[Header].apply(c)
        txs    <- c.downField("transactions").as[List[Transaction]]
        uncles <- c.downField("uncles").as[Option[List[Hash]]]
      } yield BlockWithTransactions(header, txs, uncles)
    }
  }

  @JsonCodec(decodeOnly = true) final case class Log(
    address: Address, topics: List[Bytes32], data: Array[Byte], blockNumber: Option[Long], transactionHash: Option[Hash],
    transactionIndex: Option[Int], blockHash: Option[Hash], logIndex: Option[Int], removed: Boolean
  )

  @JsonCodec(decodeOnly = true) final case class TransactionReceipt(
    transactionHash: Hash, transactionIndex: Int, blockHash: Hash, blockNumber: Long,
    cumulativeGasUsed: Long, gasUsed: Long, contractAddress: Option[Address], root: Option[Hash],
    status: Option[String], from: Address, to: Option[Address], logs: List[Log], logsBloom: String
  ) {
    // 1: succeed; 0: failure
    def txSucceed: Boolean = status.fold(false)(v => Hex.hex2Int(v) == 1)
  }

  // Option filed because of pending transaction
  @JsonCodec(decodeOnly = true)
  final case class Transaction(
    blockHash: Option[Hash], blockNumber: Option[Long], from: Address, gas: Long, gasPrice: BigInt,
    hash: Hash, input: Array[Byte], nonce: Long, to: Option[Address], transactionIndex: Option[Int],
    value: BigInt, v: String, r: String, s: String
  )

  final case class Work(hash: Hash, seedHash: Hash, target: Bytes32)

  object Work {
    implicit val decoder: Decoder[Work] = (c: HCursor) => {
      c.downArray
      for {
        lst      <- c.as[List[String]]
        hash     = Hash(lst.head)
        seedHash = Hash(lst(1))
        target   = Bytes32.from(lst(2))
      } yield Work(hash, seedHash, target)
    }
  }
}
