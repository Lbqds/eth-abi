package ethabi.protocol

import io.circe.{Json, Decoder}

case class Response(jsonrpc: String, id: Int, result: Json, error: Option[ResponseError]) {
  private val response: Either[ResponseError, Json] = if (error.isDefined) Left(error.get) else Right(result)
  def as[T : Decoder]: Either[ResponseError, Option[T]] = response.map { json =>
    if (json.isNull) None
    else json.as[T] match {
      case Left(decodingFailure) => throw decodingFailure
      case Right(value) => Some(value)
    }
  }

  // auto generated Decoder[Either[A, B]] doesn't work, construct with `either` op manually
  def decodeWith[T](decoder: Decoder[T]): Either[ResponseError, Option[T]] = response.map { json =>
    if (json.isNull) None
    else decoder.decodeJson(json) match {
      case Left(decodingFailure) => throw decodingFailure
      case Right(value) => Some(value)
    }
  }
}

case class ResponseError(code: Int, message: String, data: Option[Json])

object Response {
  case class Syncing(startingBlock: Long, currentBlock: Long, highestBlock: Long)
  case class Header(parentHash: String, sha3Uncles: String, miner: String, stateRoot: String, transactionsRoot: String,
                    receiptsRoot: String, logsBloom: String, difficulty: String, number: String, gasLimit: String, gasUsed: String,
                    timestamp: String, extraData: String, mixHash: String, nonce: String, hash:String)
  case class Log(address: String, topics: Seq[String], data: String, blockNumber: String, transactionHash: String,
                 transactionIndex: String, blockHash: String, logIndex: String, removed: Boolean)
  case class SyncProgress(startingBlock: Long, currentBlock: Long, highestBlock: Long, pulledStates: Long, knownStates: Long)
  case class SyncStatus(syncing: Boolean, status: Option[SyncProgress])
  case class TransactionReceipt(transactionHash: String, transactionIndex: String, blockHash: String, blockNumber: String,
                                cumulativeGasUsed: String, gasUsed: String, contractAddress: Option[String], root: Option[String],
                                status: Option[String], from: String, to: Option[String], logs: Seq[Log], logsBloom: String)
  case class Transaction(blockHash: Option[String], blockNumber: Option[String], from: String, gas: String, gasPrice: String,
                         hash: String, input: String, nonce: String, to: String, transactionIndex: String, value: String,
                         v: String, r: String, s: String)
}
