## eth-abi

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.lbqds/ethabi_2.13/badge.svg)](https://search.maven.org/artifact/com.github.lbqds/ethabi_2.13/0.3.0/jar)
[![Build Status](https://travis-ci.com/Lbqds/eth-abi.svg?token=iUBC3d9KBxXjFrs9989Y&branch=master)](https://travis-ci.com/Lbqds/eth-abi)

jdk 11+ because of [http4s-jdk-http-client](https://github.com/http4s/http4s-jdk-http-client)

`eth-abi` is currently available for scala 2.12 and scala2.13

### Getting Start

to begin using `eth-abi`, add the following to your `build.sbt`:

```scala
libraryDependencies ++= Seq(
  "com.github.lbqds" %% "ethabi" % "0.3.0",
  "org.typelevel" %% "cats-core" % "2.1.1",
  "org.typelevel" %% "cats-effect" % "2.1.3"
)
```

### Code Generator 

`eth-abi` have a tool which can generate scala code by solidity contract abi and bin code.

download latest version `abi-codegen-0.3.0` from the [release](https://github.com/Lbqds/eth-abi/releases) page, and execute:

```shell
$ abi-codegen-0.3.0 --help
```

use [KVStore](https://github.com/Lbqds/eth-abi/blob/master/examples/src/main/resources/KVStore.abi) contract as an example, execute:

```shell
$ abi-codegen-0.3.0 gen -a KVStore.abi -b KVStore.bin -p "examples.kvstore" -c "KVStore" -o ./
```

would generate scala code at the current directory. you can also dive into generated code at [here](https://github.com/Lbqds/eth-abi/blob/master/examples/src/main/scala/examples/kvstore/KVStore.scala).

now we can call generated scala method instead of execute contract method by `eth_sendTransaction` or `eth_sendRawTransaction`:

```scala
object Main extends IOApp {

  private def log(str: String): IO[Unit] = IO.delay(println(s"${Thread.currentThread.getName}, $str"))

  override def run(args: List[String]): IO[ExitCode] = {
    val sender = Address("60f7947aef8bbc9bc314a9b8db8096099345fba3")
    val transactionOpt = TransactionOpt(Some(400000), Some(1000), None, None)
    val retryPolicy = retry.RetryPolicies.limitRetries[IO](5).join(RetryPolicies.constantDelay[IO](5 seconds))
    KVStore[IO]("ws://127.0.0.1:8546").use { kvStore =>
      val task = for {
        client     <- kvStore.client
        peerCount  <- client.peerCount.flatMap(_.get)
        _          <- log(s"peer count: $peerCount")
        cliVersion <- client.clientVersion.flatMap(_.get)
        _          <- log(s"client version: $cliVersion")
        work       <- client.getWork.flatMap(_.get)
        _          <- log(s"work response: $work")
        protocolV  <- client.protocolVersion.flatMap(_.get)
        _          <- log(s"protocol version: $protocolV")
        coinbase   <- client.coinbase.flatMap(_.get)
        _          <- log(s"coinbase address: $coinbase")
        syncStatus <- client.syncing.flatMap(_.get)
        _          <- log(s"sync status: $syncStatus")
        deployHash <- kvStore.deploy(sender, transactionOpt)
        address    <- retryUntil[IO, Option[Address]]("wait contract deployed", retryPolicy, kvStore.address, _.isDefined).map(_.get)
        _          <- log(s"contract deploy succeed, address: $address")
        contractTx <- deployHash.get.flatMap(client.getTransactionByHash).flatMap(_.get)
        _          <- log(s"contract deploy tx: ${contractTx.get}")
        result     <- kvStore.subscribeRecord
        _          <- log(s"subscription id: ${result.id}")
        fiber      <- result.stream.forall { event =>
          println(event)
          true
        }.compile.drain.start
        txHash     <- kvStore.set(Uint16(12), DynamicBytes.from("0x010203040506070809"), sender, transactionOpt).flatMap(_.get)
        receipt    <- retryUntil[IO, Option[TransactionReceipt]](
          "wait tx receipt",
          retryPolicy,
          client.getTransactionReceipt(txHash).flatMap(_.get),
          _.isDefined
        ).map(_.get)
        _          <- log(s"tx receipt: $receipt")
        result     <- kvStore.get(Uint16(12), sender, transactionOpt)
        _          <- log(s"key: 12, value: $result")
        _          <- fiber.cancel
        _          <- log("quit now")
      } yield ()
      task.handleErrorWith(exp => IO.delay(exp.printStackTrace())) *> IO.delay(ExitCode.Success)
    }
  }
}
```

**NOTE**:

* the generated code use websocket client rather than http, because it will subscribe solidity event with [ethereum RPC PUB/SUB](https://github.com/ethereum/go-ethereum/wiki/RPC-PUB-SUB)
* you need to assure the account have been unlocked before deploy and call contract method
* every `event` will have a generated `subscribeEventName` method, which return `F[Stream[F, Event]]`

### ABIEncoderV2

`eth-abi` support [experimental ABIEncoderV2](https://solidity.readthedocs.io/en/latest/abi-spec.html#handling-tuple-types) feature, 
tuple will map to `TupleTypeN`, the generated [exchange](https://github.com/Lbqds/eth-abi/blob/master/examples/src/main/scala/examples/exchange/Exchange.scala) 
use this feature heavily.

### JSONRPC

`eth-abi` can also be used to interact directly with ethereum:

```scala
object HttpClientTest extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {
    import HttpClient._
    import scala.concurrent.duration._

    apply[IO]("http://127.0.0.1:8545").use { client =>
      for {
        cVersion  <- client.clientVersion.flatMap(_.get)
        _         <- IO.delay(println(s"client version: $cVersion"))
        nVersion  <- client.netVersion.flatMap(_.get)
        _         <- IO.delay(println(s"net version: $nVersion"))
        filterId  <- client.newBlockFilter.flatMap(_.get)
        _         <- IO.delay(println(s"filter id: $filterId"))
        changes1  <- IO.sleep(3 seconds) *> client.getFilterChangeHashes(filterId).flatMap(_.get)
        _         <- IO.delay(println(s"changes: $changes1"))
        changes2  <- IO.sleep(3 seconds) *> client.getFilterChangeHashes(filterId).flatMap(_.get)
        _         <- IO.delay(println(s"changes: $changes2"))
      } yield ExitCode.Success
    }
  }
}
```

all supported JSONRPC api list at [here](https://github.com/Lbqds/eth-abi/blob/master/ethabi/src/main/scala/ethabi/protocol/Client.scala).

### Functional Programming

`eth-abi` use [cats](https://github.com/typelevel/cats) and [cats-effect](https://github.com/typelevel/cats-effect), 
although the above example use `cats-effect` IO, you can also choose [ZIO](https://github.com/zio/zio)

and all generated scala code are functional style.

## License

Copyright (c) 2020 Lbqds

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

