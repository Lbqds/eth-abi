package codegen

class AbiDefinitionSpec extends WordSpec with Matchers {
  "test gen const function" in {
    val json =
      """
        |{
        |  "constant": true,
        |  "inputs": [
        |    {
        |      "name": "",
        |      "type": "bytes32"
        |    }
        |  ],
        |  "name": "filled",
        |  "outputs": [
        |    {
        |      "name": "",
        |      "type": "uint256"
        |    }
        |  ],
        |  "payable": false,
        |  "stateMutability": "view",
        |  "type": "function"
        |}
      """.stripMargin
    val entity = AbiDefinition(json)
    entity.name shouldBe Some("filled")
    entity.`type` shouldBe "function"
  }

  "test gen non-const function" in {
    val json =
      """
        |{
        |  "constant": false,
        |  "inputs": [
        |    {
        |      "components": [
        |        {
        |          "name": "makerAddress",
        |          "type": "address"
        |        },
        |        {
        |          "name": "takerAddress",
        |          "type": "address"
        |        },
        |        {
        |          "name": "feeRecipientAddress",
        |          "type": "address"
        |        },
        |        {
        |          "name": "senderAddress",
        |          "type": "address"
        |        },
        |        {
        |          "name": "makerAssetAmount",
        |          "type": "uint256"
        |        },
        |        {
        |          "name": "takerAssetAmount",
        |          "type": "uint256"
        |        },
        |        {
        |          "name": "makerFee",
        |          "type": "uint256"
        |        },
        |        {
        |          "name": "takerFee",
        |          "type": "uint256"
        |        },
        |        {
        |          "name": "expirationTimeSeconds",
        |          "type": "uint256"
        |        },
        |        {
        |          "name": "salt",
        |          "type": "uint256"
        |        },
        |        {
        |          "name": "makerAssetData",
        |          "type": "bytes"
        |        },
        |        {
        |          "name": "takerAssetData",
        |          "type": "bytes"
        |        }
        |      ],
        |      "name": "orders",
        |      "type": "tuple[]"
        |    },
        |    {
        |      "name": "takerAssetFillAmounts",
        |      "type": "uint256[]"
        |    },
        |    {
        |      "name": "signatures",
        |      "type": "bytes[]"
        |    }
        |  ],
        |  "name": "batchFillOrders",
        |  "outputs": [
        |    {
        |      "components": [
        |        {
        |          "name": "makerAssetFilledAmount",
        |          "type": "uint256"
        |        },
        |        {
        |          "name": "takerAssetFilledAmount",
        |          "type": "uint256"
        |        },
        |        {
        |          "name": "makerFeePaid",
        |          "type": "uint256"
        |        },
        |        {
        |          "name": "takerFeePaid",
        |          "type": "uint256"
        |        }
        |      ],
        |      "name": "totalFillResults",
        |      "type": "tuple"
        |    }
        |  ],
        |  "payable": false,
        |  "stateMutability": "nonpayable",
        |  "type": "function"
        |}
      """.stripMargin
    val entity = AbiDefinition(json)
    entity.signature.get shouldBe "batchFillOrders((address,address,address,address,uint256,uint256,uint256,uint256,uint256,uint256,bytes,bytes)[],uint256[],bytes[])"
    Hex.bytes2Hex(entity.identifier.get) shouldBe "297bb70b"
  }

  "test function signature" in {
    val json =
      """
        |{
        |  "name": "f",
        |  "type": "function",
        |  "inputs": [
        |    {
        |      "name": "s",
        |      "type": "tuple",
        |      "components": [
        |        {
        |          "name": "a",
        |          "type": "uint256"
        |        },
        |        {
        |          "name": "b",
        |          "type": "uint256[]"
        |        },
        |        {
        |          "name": "c",
        |          "type": "tuple[4]",
        |          "components": [
        |            {
        |              "name": "x",
        |              "type": "uint256"
        |            },
        |            {
        |              "name": "y",
        |              "type": "uint256"
        |            }
        |          ]
        |        }
        |      ]
        |    },
        |    {
        |      "name": "t",
        |      "type": "tuple",
        |      "components": [
        |        {
        |          "name": "x",
        |          "type": "uint256"
        |        },
        |        {
        |          "name": "y",
        |          "type": "uint256"
        |        }
        |      ]
        |    },
        |    {
        |      "name": "a",
        |      "type": "uint256"
        |    }
        |  ],
        |  "outputs": []
        |}
      """.stripMargin
    val entity = AbiDefinition(json)
    entity.signature.get shouldBe "f((uint256,uint256[],(uint256,uint256)[4]),(uint256,uint256),uint256)"
    Hex.bytes2Hex(entity.identifier.get) shouldBe "df297bad"
  }
}
