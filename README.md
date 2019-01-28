## eth-abi

[![Build Status](https://travis-ci.com/Lbqds/eth-abi.svg?token=iUBC3d9KBxXjFrs9989Y&branch=master)](https://travis-ci.com/Lbqds/eth-abi)

generate scala code from solidity contract

### codegen 

download from the [release](https://github.com/Lbqds/eth-abi/releases) page, then execute:

```
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

there are some generated [examples](https://github.com/Lbqds/eth-abi/tree/master/examples/src/main/scala/examples)

### ABIEncoderV2

`eth-abi` support [experimental ABIEncoderV2](https://solidity.readthedocs.io/en/latest/abi-spec.html#handling-tuple-types) feature, 
tuple will map to `TupleTypeN`, the generated [exchange](https://github.com/Lbqds/eth-abi/blob/master/examples/src/main/scala/examples/exchange/Exchange.scala) 
use this feature heavily.

### Sonatype

`eth-abi` can also be used to interact directly with ethereum, please wait to publish
