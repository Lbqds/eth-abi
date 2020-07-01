// AUTO GENERATED, DO NOT EDIT

package examples.token
import ethabi.util._
import ethabi.types._
import ethabi.types.generated._
import ethabi.protocol._
import ethabi.protocol.Request._
import ethabi.protocol.Response.Log
import ethabi.protocol.Subscription.SubscriptionResult
import cats.implicits._
import cats.Applicative
import cats.effect._
import cats.effect.concurrent._
final class Token[F[_]: ConcurrentEffect: Timer] private (private val impl: Contract[F]) { self =>
  private val binary = "60806040523480156200001157600080fd5b506040805190810160405280600b81526020017f53696d706c65546f6b656e0000000000000000000000000000000000000000008152506040805190810160405280600381526020017f53494d0000000000000000000000000000000000000000000000000000000000815250601282600390805190602001906200009892919062000293565b508160049080519060200190620000b192919062000293565b5080600560006101000a81548160ff021916908360ff160217905550505050620000f633601260ff16600a0a61271002620000fc640100000000026401000000009004565b62000342565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff16141515156200013957600080fd5b6200015e81600254620002716401000000000262001159179091906401000000009004565b600281905550620001c5816000808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054620002716401000000000262001159179091906401000000009004565b6000808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff16600073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35050565b60008082840190508381101515156200028957600080fd5b8091505092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620002d657805160ff191683800117855562000307565b8280016001018555821562000307579182015b8281111562000306578251825591602001919060010190620002e9565b5b5090506200031691906200031a565b5090565b6200033f91905b808211156200033b57600081600090555060010162000321565b5090565b90565b6111a680620003526000396000f3fe608060405234801561001057600080fd5b50600436106100ec576000357c010000000000000000000000000000000000000000000000000000000090048063313ce567116100a957806395d89b411161008357806395d89b41146103a2578063a457c2d714610425578063a9059cbb1461048b578063dd62ed3e146104f1576100ec565b8063313ce567146102c057806339509351146102e457806370a082311461034a576100ec565b806306fdde03146100f1578063095ea7b31461017457806318160ddd146101da57806323b872dd146101f85780632e0f26251461027e5780632ff2e9dc146102a2575b600080fd5b6100f9610569565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561013957808201518184015260208101905061011e565b50505050905090810190601f1680156101665780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101c06004803603604081101561018a57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291908035906020019092919050505061060b565b604051808215151515815260200191505060405180910390f35b6101e2610738565b6040518082815260200191505060405180910390f35b6102646004803603606081101561020e57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610742565b604051808215151515815260200191505060405180910390f35b61028661094a565b604051808260ff1660ff16815260200191505060405180910390f35b6102aa61094f565b6040518082815260200191505060405180910390f35b6102c861095e565b604051808260ff1660ff16815260200191505060405180910390f35b610330600480360360408110156102fa57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610975565b604051808215151515815260200191505060405180910390f35b61038c6004803603602081101561036057600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610bac565b6040518082815260200191505060405180910390f35b6103aa610bf4565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156103ea5780820151818401526020810190506103cf565b50505050905090810190601f1680156104175780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6104716004803603604081101561043b57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610c96565b604051808215151515815260200191505060405180910390f35b6104d7600480360360408110156104a157600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610ecd565b604051808215151515815260200191505060405180910390f35b6105536004803603604081101561050757600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610ee4565b6040518082815260200191505060405180910390f35b606060038054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106015780601f106105d657610100808354040283529160200191610601565b820191906000526020600020905b8154815290600101906020018083116105e457829003601f168201915b5050505050905090565b60008073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff161415151561064857600080fd5b81600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925846040518082815260200191505060405180910390a36001905092915050565b6000600254905090565b60006107d382600160008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054610f6b90919063ffffffff16565b600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555061085e848484610f8d565b3373ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925600160008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546040518082815260200191505060405180910390a3600190509392505050565b601281565b601260ff16600a0a6127100281565b6000600560009054906101000a900460ff16905090565b60008073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff16141515156109b257600080fd5b610a4182600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461115990919063ffffffff16565b600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546040518082815260200191505060405180910390a36001905092915050565b60008060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020549050919050565b606060048054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610c8c5780601f10610c6157610100808354040283529160200191610c8c565b820191906000526020600020905b815481529060010190602001808311610c6f57829003601f168201915b5050505050905090565b60008073ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff1614151515610cd357600080fd5b610d6282600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054610f6b90919063ffffffff16565b600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508273ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020546040518082815260200191505060405180910390a36001905092915050565b6000610eda338484610f8d565b6001905092915050565b6000600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054905092915050565b6000828211151515610f7c57600080fd5b600082840390508091505092915050565b600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614151515610fc957600080fd5b61101a816000808673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054610f6b90919063ffffffff16565b6000808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506110ad816000808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461115990919063ffffffff16565b6000808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508173ffffffffffffffffffffffffffffffffffffffff168373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a3505050565b600080828401905083811015151561117057600080fd5b809150509291505056fea165627a7a7230582008f3a090533ddacdd7c8c5ec44ffd7b040994ae055e7f95b00e42d19cbc274e20029"
  def client: F[Client[F]] = impl.client
  def subscriber: F[Subscriber[F]] = impl.subscriber
  def isDeployed: F[Boolean] = impl.isDeployed
  def address: F[Option[Address]] = impl.address
  def loadFrom(address: Address): F[Unit] = impl.load(address)
  def name(sender: Address, opt: TransactionOpt): F[TupleType1[StringType]] = {
    val encoded = Hex.hex2Bytes("06fdde03")
    for (promise <- impl.call(CallArgs(encoded, sender, opt)); data <- promise.get) yield {
      val result = TypeInfo[TupleType1[StringType]].decode(data, 0)
      result._1
    }
  }
  def approve(spender: Address, value: Uint256, sender: Address, opt: TransactionOpt): F[Deferred[F, Hash]] = {
    val paramsEncoded = TypeInfo[TupleType2[Address, Uint256]].encode(TupleType2.apply[Address, Uint256](spender, value))
    val functionId = Hex.hex2Bytes("095ea7b3")
    val encoded = functionId ++ paramsEncoded
    impl.sendTransaction(CallArgs(encoded, sender, opt))
  }
  def totalSupply(sender: Address, opt: TransactionOpt): F[TupleType1[Uint256]] = {
    val encoded = Hex.hex2Bytes("18160ddd")
    for (promise <- impl.call(CallArgs(encoded, sender, opt)); data <- promise.get) yield {
      val result = TypeInfo[TupleType1[Uint256]].decode(data, 0)
      result._1
    }
  }
  def transferFrom(from: Address, to: Address, value: Uint256, sender: Address, opt: TransactionOpt): F[Deferred[F, Hash]] = {
    val paramsEncoded = TypeInfo[TupleType3[Address, Address, Uint256]].encode(TupleType3.apply[Address, Address, Uint256](from, to, value))
    val functionId = Hex.hex2Bytes("23b872dd")
    val encoded = functionId ++ paramsEncoded
    impl.sendTransaction(CallArgs(encoded, sender, opt))
  }
  def DECIMALS(sender: Address, opt: TransactionOpt): F[TupleType1[Uint8]] = {
    val encoded = Hex.hex2Bytes("2e0f2625")
    for (promise <- impl.call(CallArgs(encoded, sender, opt)); data <- promise.get) yield {
      val result = TypeInfo[TupleType1[Uint8]].decode(data, 0)
      result._1
    }
  }
  def INITIAL_SUPPLY(sender: Address, opt: TransactionOpt): F[TupleType1[Uint256]] = {
    val encoded = Hex.hex2Bytes("2ff2e9dc")
    for (promise <- impl.call(CallArgs(encoded, sender, opt)); data <- promise.get) yield {
      val result = TypeInfo[TupleType1[Uint256]].decode(data, 0)
      result._1
    }
  }
  def decimals(sender: Address, opt: TransactionOpt): F[TupleType1[Uint8]] = {
    val encoded = Hex.hex2Bytes("313ce567")
    for (promise <- impl.call(CallArgs(encoded, sender, opt)); data <- promise.get) yield {
      val result = TypeInfo[TupleType1[Uint8]].decode(data, 0)
      result._1
    }
  }
  def increaseAllowance(spender: Address, addedValue: Uint256, sender: Address, opt: TransactionOpt): F[Deferred[F, Hash]] = {
    val paramsEncoded = TypeInfo[TupleType2[Address, Uint256]].encode(TupleType2.apply[Address, Uint256](spender, addedValue))
    val functionId = Hex.hex2Bytes("39509351")
    val encoded = functionId ++ paramsEncoded
    impl.sendTransaction(CallArgs(encoded, sender, opt))
  }
  def balanceOf(owner: Address, sender: Address, opt: TransactionOpt): F[TupleType1[Uint256]] = {
    val paramsEncoded = TypeInfo[TupleType1[Address]].encode(TupleType1.apply[Address](owner))
    val functionId = Hex.hex2Bytes("70a08231")
    val encoded = functionId ++ paramsEncoded
    for (promise <- impl.call(CallArgs(encoded, sender, opt)); data <- promise.get) yield {
      val result = TypeInfo[TupleType1[Uint256]].decode(data, 0)
      result._1
    }
  }
  def symbol(sender: Address, opt: TransactionOpt): F[TupleType1[StringType]] = {
    val encoded = Hex.hex2Bytes("95d89b41")
    for (promise <- impl.call(CallArgs(encoded, sender, opt)); data <- promise.get) yield {
      val result = TypeInfo[TupleType1[StringType]].decode(data, 0)
      result._1
    }
  }
  def decreaseAllowance(spender: Address, subtractedValue: Uint256, sender: Address, opt: TransactionOpt): F[Deferred[F, Hash]] = {
    val paramsEncoded = TypeInfo[TupleType2[Address, Uint256]].encode(TupleType2.apply[Address, Uint256](spender, subtractedValue))
    val functionId = Hex.hex2Bytes("a457c2d7")
    val encoded = functionId ++ paramsEncoded
    impl.sendTransaction(CallArgs(encoded, sender, opt))
  }
  def transfer(to: Address, value: Uint256, sender: Address, opt: TransactionOpt): F[Deferred[F, Hash]] = {
    val paramsEncoded = TypeInfo[TupleType2[Address, Uint256]].encode(TupleType2.apply[Address, Uint256](to, value))
    val functionId = Hex.hex2Bytes("a9059cbb")
    val encoded = functionId ++ paramsEncoded
    impl.sendTransaction(CallArgs(encoded, sender, opt))
  }
  def allowance(owner: Address, spender: Address, sender: Address, opt: TransactionOpt): F[TupleType1[Uint256]] = {
    val paramsEncoded = TypeInfo[TupleType2[Address, Address]].encode(TupleType2.apply[Address, Address](owner, spender))
    val functionId = Hex.hex2Bytes("dd62ed3e")
    val encoded = functionId ++ paramsEncoded
    for (promise <- impl.call(CallArgs(encoded, sender, opt)); data <- promise.get) yield {
      val result = TypeInfo[TupleType1[Uint256]].decode(data, 0)
      result._1
    }
  }
  def deploy(sender: Address, opt: TransactionOpt): F[Deferred[F, Hash]] = {
    val encoded = Hex.hex2Bytes(binary)
    impl.deploy(CallArgs(encoded, sender, opt))
  }
  private def decodeTransfer(log: Log): Event = {
    val typeInfo14 = TypeInfo[Address]
    val typeInfo15 = TypeInfo[Address]
    val typeInfo16 = TypeInfo[TupleType1[Uint256]]
    val typeInfos: List[TypeInfo[SolType]] = List(typeInfo14, typeInfo15, typeInfo16)
    Event.decode(typeInfos, log)
  }
  def subscribeTransfer: F[SubscriptionResult[F, Event]] = {
    for (result <- impl.subscribeLogs(Hash("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef"))) yield SubscriptionResult[F, Event](result.id, result.stream.map(decodeTransfer))
  }
  private def decodeApproval(log: Log): Event = {
    val typeInfo17 = TypeInfo[Address]
    val typeInfo18 = TypeInfo[Address]
    val typeInfo19 = TypeInfo[TupleType1[Uint256]]
    val typeInfos: List[TypeInfo[SolType]] = List(typeInfo17, typeInfo18, typeInfo19)
    Event.decode(typeInfos, log)
  }
  def subscribeApproval: F[SubscriptionResult[F, Event]] = {
    for (result <- impl.subscribeLogs(Hash("0x8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925"))) yield SubscriptionResult[F, Event](result.id, result.stream.map(decodeApproval))
  }
}
object Token {
  def apply[F[_]: ConcurrentEffect: Timer](endpoint: String)(implicit CS: ContextShift[F]): Resource[F, Token[F]] = {
    Contract[F](endpoint).flatMap(impl => Resource.liftF(Applicative[F].pure(new Token(impl))))
  }
}