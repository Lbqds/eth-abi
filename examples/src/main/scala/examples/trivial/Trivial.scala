// AUTO GENERATED, DO NOT EDIT

package examples.trivial
import akka.NotUsed
import akka.stream.scaladsl.Source
import ethabi.util.{ Hex, Hash }
import ethabi.types._
import ethabi.types.generated._
import ethabi.protocol.{ Contract, EventValue }
import ethabi.protocol.Request._
import ethabi.protocol.Response.Log
import scala.concurrent.Future
final class Trivial(endpoint: String) { self =>
  private val impl = Contract(endpoint)
  import impl.dispatcher
  def service = impl.service
  private val binary = "608060405234801561001057600080fd5b50610374806100206000396000f300608060405260043610610041576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806385799c3614610046575b600080fd5b34801561005257600080fd5b5061006d600480360361006891908101906101cb565b61006f565b005b806040015181600001517f174e799db4473820d2ac095205358bba921759d7f2fb09509751a40a543842aa836020015184606001516040516100b2929190610242565b60405180910390a350565b600082601f83011215156100d057600080fd5b81356100e36100de826102a6565b610279565b915080825260208301602083018583830111156100ff57600080fd5b61010a8382846102e7565b50505092915050565b60006080828403121561012557600080fd5b61012f6080610279565b9050600061013f848285016101b7565b600083015250602082013567ffffffffffffffff81111561015f57600080fd5b61016b848285016100bd565b602083015250604061017f848285016101b7565b604083015250606082013567ffffffffffffffff81111561019f57600080fd5b6101ab848285016100bd565b60608301525092915050565b60006101c382356102dd565b905092915050565b6000602082840312156101dd57600080fd5b600082013567ffffffffffffffff8111156101f757600080fd5b61020384828501610113565b91505092915050565b6000610217826102d2565b80845261022b8160208601602086016102f6565b61023481610329565b602085010191505092915050565b6000604082019050818103600083015261025c818561020c565b90508181036020830152610270818461020c565b90509392505050565b6000604051905081810181811067ffffffffffffffff8211171561029c57600080fd5b8060405250919050565b600067ffffffffffffffff8211156102bd57600080fd5b601f19601f8301169050602081019050919050565b600081519050919050565b6000819050919050565b82818337600083830152505050565b60005b838110156103145780820151818401526020810190506102f9565b83811115610323576000848401525b50505050565b6000601f19601f83011690509190505600a265627a7a723058201ce538708976bc5eb642066442b9156e9bf442c7f38e3977bac329d2191e7b236c6578706572696d656e74616cf50037"
  def trigger(t: TupleType4[Uint256, DynamicBytes, Uint256, DynamicBytes], sender: Address, opt: TransactionOpt): Future[Hash] = {
    val paramsEncoded = implicitly[TypeInfo[TupleType1[TupleType4[Uint256, DynamicBytes, Uint256, DynamicBytes]]]].encode(TupleType1.apply[TupleType4[Uint256, DynamicBytes, Uint256, DynamicBytes]](t))
    val functionId = Hex.hex2Bytes("85799c36")
    val encoded = functionId ++ paramsEncoded
    impl.sendTransaction(encoded, sender, opt)
  }
  def deploy(sender: Address, opt: TransactionOpt): Unit = {
    val encoded = Hex.hex2Bytes(binary)
    impl.deploy(encoded, sender, opt)
  }
  def decodeTestEvent(log: Log): EventValue = {
    var typeInfos = Seq.empty[TypeInfo[SolType]]
    typeInfos = typeInfos :+ (implicitly[TypeInfo[Uint256]])
    typeInfos = typeInfos :+ (implicitly[TypeInfo[Uint256]])
    typeInfos = typeInfos :+ (implicitly[TypeInfo[TupleType2[DynamicBytes, DynamicBytes]]])
    EventValue.decodeEvent(typeInfos, log)
  }
  def subscribeTestEvent: Source[EventValue, NotUsed] = {
    val query = LogQuery.from(contractAddress, Hash("0x174e799db4473820d2ac095205358bba921759d7f2fb09509751a40a543842aa"))
    impl.subscribeLogs(query).map(decodeTestEvent)
  }
  def isDeployed: Boolean = impl.isDeployed
  def contractAddress: Address = impl.address.get
  def loadFrom(contractAddress: Address) = impl.load(contractAddress)
}