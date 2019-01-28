package codegen

class ParamSpec extends WordSpec with Matchers {
  "test deserialize argument(without components)" in {
    val json = """{"name":"balance","type":"uint256"}"""
    val argument = decode[Param](json).right.get
    argument.name shouldBe "balance"
    argument.`type` shouldBe "uint256"
    argument.components shouldBe None
    argument.indexed shouldBe None
  }

  "test serialize argument(without components)" in {
    val argument = Param(name = "balance", `type` = "uint256", components = None, indexed = None)
    val json = argument.asJson.noSpaces
    json shouldBe """{"name":"balance","type":"uint256","components":null,"indexed":null}"""
  }

  "test deserialize argument(with components)" in {
    val json = """{"name":"account","type":"tuple","components":[{"name":"name","type":"string"}],"indexed":null}"""
    val argument = decode[Param](json).right.get
    argument.name shouldBe "account"
    argument.`type` shouldBe "tuple"
    argument.components.get.head.name shouldBe "name"
    argument.components.get.head.`type` shouldBe "string"
    argument.indexed shouldBe None
  }

  "test serialize argument(with components)" in {
    val argument = Param("account", "tuple", Some(Seq(Param("name", "string"))), None)
    val json = argument.asJson.noSpaces
    json shouldBe """{"name":"account","type":"tuple","components":[{"name":"name","type":"string","components":null,"indexed":null}],"indexed":null}"""
  }

  "test deserialize nested arguments" in {
    val json =
      """
        |{
        |  "name": "s",
        |  "type": "tuple",
        |  "components": [
        |    {
        |      "name": "a",
        |      "type": "uint256"
        |    },
        |    {
        |      "name": "b",
        |      "type": "uint256[]"
        |    },
        |    {
        |      "name": "c",
        |      "type": "tuple[]",
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
        |    }
        |  ]
        |}
      """.stripMargin
    val result = decode[Param](json).right.get
    val components = result.components.get
    components.length shouldBe 3
    val nested = components(2)
    nested.components.get.length shouldBe 2
    nested.`type` shouldBe "tuple[]"
    nested.name shouldBe "c"
  }

  "test param type" in {
    val json = """{"name":"array","type":"uint16[4][5]"}"""
    val param = Param(json)
    param.tpe.syntax shouldBe "StaticArray[StaticArray[Uint16]]"
  }

  "test param type(with tuple)" in {
    val json =
      """
        |{
        |  "name": "whatever",
        |  "type": "tuple",
        |  "components": [
        |    {"name": "whatever", "type": "uint"},
        |    {"name": "whatever", "type": "int"},
        |    {"name": "whatever", "type": "bytes"}
        |  ]
        |}
      """.stripMargin
    val param = Param(json)
    param.tpe.syntax shouldBe "TupleType3[Uint256, Int256, DynamicBytes]"
  }

  "test param type(with nested params)" in {
    val json =
      """
        |{
        |  "name": "s",
        |  "type": "tuple",
        |  "components": [
        |    {
        |      "name": "a",
        |      "type": "bytes12"
        |    },
        |    {
        |      "name": "b",
        |      "type": "address[]"
        |    },
        |    {
        |      "name": "c",
        |      "type": "tuple[]",
        |      "components": [
        |        {
        |          "name": "x",
        |          "type": "bool"
        |        },
        |        {
        |          "name": "y",
        |          "type": "string"
        |        }
        |      ]
        |    },
        |    {
        |      "name": "d",
        |      "type": "string[]"
        |    }
        |  ]
        |}
      """.stripMargin
    val param = Param(json)
    param.tpe.syntax shouldBe "TupleType4[Bytes12, DynamicArray[Address], DynamicArray[TupleType2[Bool, StringType]], DynamicArray[StringType]]"
  }

  "test param signature(with nested tuple dynamic array)" in {
    val json =
      """
        |{
        |  "name": "s",
        |  "type": "tuple",
        |  "components": [
        |    {
        |      "name": "a",
        |      "type": "uint256"
        |    },
        |    {
        |      "name": "b",
        |      "type": "uint256[]"
        |    },
        |    {
        |      "name": "c",
        |      "type": "tuple[]",
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
        |    }
        |  ]
        |}
      """.stripMargin
    val param = Param(json)
    param.signature shouldBe "(uint256,uint256[],(uint256,uint256)[])"
  }

  "test param signature(with nested tuple static array)" in {
    val json =
      """
        |{
        |  "name": "s",
        |  "type": "tuple",
        |  "components": [
        |    {
        |      "name": "a",
        |      "type": "uint256"
        |    },
        |    {
        |      "name": "b",
        |      "type": "uint256[]"
        |    },
        |    {
        |      "name": "c",
        |      "type": "tuple[4]",
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
        |    }
        |  ]
        |}
      """.stripMargin
    val param = Param(json)
    param.signature shouldBe "(uint256,uint256[],(uint256,uint256)[4])"
  }
}