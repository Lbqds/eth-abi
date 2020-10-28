// AUTO GENERATED, DO NOT EDIT

package examples.kvstore
import ethabi.util._
import ethabi.types._
import ethabi.types.generated._
import ethabi.protocol._
import ethabi.protocol.ws.WebsocketClient
import ethabi.protocol.Request._
import ethabi.protocol.Response.Log
import ethabi.protocol.Subscription.SubscriptionResult
import cats.implicits._
import cats.Applicative
import cats.effect._
import cats.effect.concurrent._
final class KVStore[F[_]: ConcurrentEffect: Timer] private (private val impl: Contract[F]) { self =>
  private val binary = "6080604052610542806100136000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630df584f11461005c578063385ddf0014610106578063b3fd15a014610170575b600080fd5b34801561006857600080fd5b5061008b600480360381019080803561ffff16906020019092919050505061021a565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100cb5780820151818401526020810190506100b0565b50505050905090810190601f1680156100f85780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b61016e600480360381019080803561ffff169060200190929190803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506102ca565b005b34801561017c57600080fd5b5061019f600480360381019080803561ffff1690602001909291905050506103b5565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101df5780820151818401526020810190506101c4565b50505050905090810190601f16801561020c5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b60006020528060005260406000206000915090508054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102c25780601f10610297576101008083540402835291602001916102c2565b820191906000526020600020905b8154815290600101906020018083116102a557829003601f168201915b505050505081565b806000808461ffff1661ffff16815260200190815260200160002090805190602001906102f8929190610471565b508161ffff163373ffffffffffffffffffffffffffffffffffffffff167f5f9f5e049bc49bc3d2067c5c5077871baab0b4e104a1554c8ac68f7c9fd81884836040518080602001828103825283818151815260200191508051906020019080838360005b8381101561037757808201518184015260208101905061035c565b50505050905090810190601f1680156103a45780820380516001836020036101000a031916815260200191505b509250505060405180910390a35050565b60606000808361ffff1661ffff1681526020019081526020016000208054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104655780601f1061043a57610100808354040283529160200191610465565b820191906000526020600020905b81548152906001019060200180831161044857829003601f168201915b50505050509050919050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106104b257805160ff19168380011785556104e0565b828001600101855582156104e0579182015b828111156104df5782518255916020019190600101906104c4565b5b5090506104ed91906104f1565b5090565b61051391905b8082111561050f5760008160009055506001016104f7565b5090565b905600a165627a7a7230582061428827ea316a5ddc2cc0c135b621c5525c61031d2556d7586f05dd0764c9810029"
  def client: F[WebsocketClient[F]] = impl.client
  def subscriber: F[Subscriber[F]] = impl.subscriber
  def isDeployed: F[Boolean] = impl.isDeployed
  def address: F[Option[Address]] = impl.address
  def loadFrom(address: Address): F[Unit] = impl.load(address)
  def data(fresh1: Uint16, sender: Address, opt: TransactionOpt): F[TupleType1[DynamicBytes]] = {
    val paramsEncoded = TypeInfo[TupleType1[Uint16]].encode(TupleType1.apply[Uint16](fresh1))
    val functionId = Hex.hex2Bytes("0df584f1")
    val encoded = functionId ++ paramsEncoded
    for (promise <- impl.call(CallArgs(encoded, sender, opt)); data <- promise.get) yield {
      val result = TypeInfo[TupleType1[DynamicBytes]].decode(data, 0)
      result._1
    }
  }
  def set(_key: Uint16, _value: DynamicBytes, sender: Address, opt: TransactionOpt): F[Deferred[F, Hash]] = {
    val paramsEncoded = TypeInfo[TupleType2[Uint16, DynamicBytes]].encode(TupleType2.apply[Uint16, DynamicBytes](_key, _value))
    val functionId = Hex.hex2Bytes("385ddf00")
    val encoded = functionId ++ paramsEncoded
    impl.sendTransaction(CallArgs(encoded, sender, opt))
  }
  def get(_key: Uint16, sender: Address, opt: TransactionOpt): F[TupleType1[DynamicBytes]] = {
    val paramsEncoded = TypeInfo[TupleType1[Uint16]].encode(TupleType1.apply[Uint16](_key))
    val functionId = Hex.hex2Bytes("b3fd15a0")
    val encoded = functionId ++ paramsEncoded
    for (promise <- impl.call(CallArgs(encoded, sender, opt)); data <- promise.get) yield {
      val result = TypeInfo[TupleType1[DynamicBytes]].decode(data, 0)
      result._1
    }
  }
  def deploy(sender: Address, opt: TransactionOpt): F[Deferred[F, Hash]] = {
    val encoded = Hex.hex2Bytes(binary)
    impl.deploy(CallArgs(encoded, sender, opt))
  }
  private def decodeRecord(log: Log): Event = {
    val typeInfo4 = TypeInfo[Address]
    val typeInfo5 = TypeInfo[Uint16]
    val typeInfo6 = TypeInfo[TupleType1[DynamicBytes]]
    val typeInfos: List[TypeInfo[SolType]] = List(typeInfo4, typeInfo5, typeInfo6)
    Event.decode(typeInfos, log)
  }
  def subscribeRecord: F[SubscriptionResult[F, Event]] = {
    for (result <- impl.subscribeLogs(Bytes32.from("0x5f9f5e049bc49bc3d2067c5c5077871baab0b4e104a1554c8ac68f7c9fd81884"))) yield SubscriptionResult[F, Event](result.id, result.stream.map(decodeRecord))
  }
}
object KVStore {
  def apply[F[_]: ConcurrentEffect: Timer](endpoint: String)(implicit CS: ContextShift[F]): Resource[F, KVStore[F]] = {
    Contract[F](endpoint).flatMap(impl => Resource.liftF(Applicative[F].pure(new KVStore(impl))))
  }
}