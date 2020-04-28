## eth-abi

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.lbqds/eth-abi_2.12/badge.svg)](https://search.maven.org/artifact/com.github.lbqds/eth-abi_2.12/0.1/jar)
[![Build Status](https://travis-ci.com/Lbqds/eth-abi.svg?token=iUBC3d9KBxXjFrs9989Y&branch=master)](https://travis-ci.com/Lbqds/eth-abi)

generate scala code from solidity contract

### Sonatype

to begin using `eth-abi`, add the following to your `build.sbt`:

```scala
libraryDependencies ++= Seq(
  "com.github.lbqds" %% "eth-abi" % "0.1",
  "com.typesafe.akka" %% "akka-actor" % "2.5.19",
  "com.typesafe.akka" %% "akka-stream" % "2.5.19"
)
```

### codegen 

download latest version `abi-codegen.tar.gz` from the [release](https://github.com/Lbqds/eth-abi/releases) page, then execute:

```shell
$ tar -xf abi-codegen.tar.gz
$ scala abi-codegen.jar --help
```

it will show usage as follow:

```text
abi-codegen 0.1
Usage: abi-codegen [options]

  -a, --abi <abiFile>          contract abi file
  -b, --bin <binFile>          contract bin file
  -p, --package <packages>     package name e.g. "examples.token"
  -c, --className <className>  class name
  -o, --output <output dir>    output directory
  -h, --help                   show usage
```

a trivial example as follow:

```solidity
pragma solidity ^0.4.24;
pragma experimental ABIEncoderV2;

contract Trivial {
  event TestEvent(uint256 indexed a, bytes b, uint256 indexed c, bytes d);
  struct T { uint256 a; bytes b; uint256 c; bytes d; }
  function Trivial() public {}
  function trigger(T t) public {
    emit TestEvent(t.a, t.b, t.c, t.d);
  }
}
```

this contract use solidity [experimental ABIEncoderV2](https://solidity.readthedocs.io/en/latest/abi-spec.html#handling-tuple-types) feature,
when we call `trigger` method, it just emit a log, the generated code at [here](https://github.com/Lbqds/eth-abi/blob/master/examples/src/main/scala/examples/trivial/Trivial.scala).
now you can interact with ethereum use the generated scala code:

```scala
implicit val system = ActorSystem()
implicit val materializer = ActorMaterializer()
import system.dispatcher

// creator of contract
val sender = Address("0xe538b17ebf20efcf1c426cf1480e8a2a4b87cb1b")
val contract = new Trivial("ws://127.0.0.1:8546")
val opt = TransactionOpt(Some(BigInt(1000000)), Some(BigInt(10000)), None, None)

contract.deploy(sender, opt)

// waiting to contract deployed
while (!contract.isDeployed) {
  Thread.sleep(1000)
}
println("deploy succeed, address is: " + contract.contractAddress)

// subscribe TestEvent
contract.subscribeTestEvent.runForeach(println)

val a = Uint256(BigInt(1000))
val b = DynamicBytes(Array[Byte](0x01, 0x02, 0x03, 0x04))
val c = Uint256(BigInt(3333))
val d = DynamicBytes(Array[Byte](0x11, 0x22, 0x33, 0x44))
val t = TupleType4[Uint256, DynamicBytes, Uint256, DynamicBytes](a, b, c, d)

// call contract trigger method
contract.trigger(t, sender, opt) onComplete {
  case Success(txHash) =>
    println("call trigger succeed: " + txHash)
    system.terminate()
  case Failure(exception) => println("call trigger failed: " + exception)
}
```

**NOTE**:

* the generated code use websocket client rather than http, because it will subscribe solidity event with [ethereum RPC PUB/SUB](https://github.com/ethereum/go-ethereum/wiki/RPC-PUB-SUB)
* you need to assure the account have been unlocked before deploy and call contract method
* every `event` will have a generated `subscribeEventName` method, which just return `Source[EventValue, NotUsed]`, the subscription start only after have been [materialized](https://doc.akka.io/docs/akka/2.5.3/scala/stream/stream-flows-and-basics.html#defining-and-running-streams)

### ABIEncoderV2

`eth-abi` support [experimental ABIEncoderV2](https://solidity.readthedocs.io/en/latest/abi-spec.html#handling-tuple-types) feature, 
tuple will map to `TupleTypeN`, the generated [exchange](https://github.com/Lbqds/eth-abi/blob/master/examples/src/main/scala/examples/exchange/Exchange.scala) 
use this feature heavily.

### JSONRPC

`eth-abi` can also be used to interact directly with ethereum:

```scala
import ethabi.protocol.http.Client
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.util.{Failure, Success}

implicit val system = ActorSystem()
implicit val materializer = ActorMaterializer()
import system.dispatcher

val client = Client("http://127.0.0.1:8545")
client.blockNumber onComplete {
  case Failure(exception) => throw exception
  case Success(resp) => resp match {
    case Left(responseError) => println(s"response error: ${responseError.message}")
    case Right(number) =>
      println(s"current block number: $number")
      system.terminate()
  }
}
```

all supported JSONRPC api list at [here](https://github.com/Lbqds/eth-abi/blob/master/src/main/scala/ethabi/protocol/Service.scala).
