package codegen

import scala.sys.process._
import ethabi.util

object GenTypes extends App {
  val header = "// AUTO GENERATED, DO NOT EDIT\n"

  def bytesTemplate(length: Int): String = {
    s"""
       |package ethabi
       |package types
       |package generated
       |import util.Hex
       |
       |final class Bytes$length(val value: Array[Byte]) extends SolType {
       |  assert(value.length <= $length)
       |  override def toString = Hex.bytes2Hex(value, withPrefix = true)
       |}
       |
       |object Bytes$length {
       |  def apply(value: Array[Byte]): Bytes$length = new Bytes$length(value)
       |  def from(value: String): Bytes$length = new Bytes$length(Hex.hex2Bytes(value))
       |
       |  implicit lazy val typeInfo: TypeInfo[Bytes$length] = new TypeInfo[Bytes$length] {
       |    override def name: String = "bytes$length"
       |    override def isStatic: Boolean = true
       |    override def encode[U >: Bytes$length](value: U): Array[Byte] = {
       |      StaticBytes.encode(value.asInstanceOf[Bytes$length].value, $length)
       |    }
       |    override def decode(bytes: Array[Byte], position: Int): (Bytes$length, Int) = {
       |      val value = StaticBytes.decode(bytes, $length, position)
       |      (Bytes$length(value), 32)
       |    }
       |  }
       |}
       |""".stripMargin
  }

  lazy val path = "pwd".!!

  def pathOf(fileName: String): String =
    path.substring(0, path.length - 1) + s"/src/main/scala/ethabi/types/generated/$fileName.scala"

  def genBytes(): Unit =
    (1 to 32).foreach(length => util.writeToFile(pathOf(s"Bytes$length"), header + bytesTemplate(length)))

  def intsTemplates(length: Int): String = {
    s"""
       |package ethabi
       |package types
       |package generated
       |
       |final class Int$length(val value: BigInt) extends SolType {
       |  assert(value.bitLength <= $length)
       |  override def toString = value.toString
       |}
       |
       |object Int$length {
       |  def apply(value: BigInt): Int$length = new Int$length(value)
       |  def from(value: String): Int$length = Int$length(BigInt(value))
       |
       |  implicit lazy val typeInfo: TypeInfo[Int$length] = new TypeInfo[Int$length] {
       |    override def name: String = "int$length"
       |    override def isStatic: Boolean = true
       |    override def encode[U >: Int$length](value: U): Array[Byte] = {
       |      IntType.encode(value.asInstanceOf[Int$length].value)
       |    }
       |    override def decode(bytes: Array[Byte], position: Int): (Int$length, Int) = {
       |      val value = IntType.decode(bytes, $length, position)
       |      (Int$length(value), 32)
       |    }
       |  }
       |}
       |""".stripMargin
  }

  def genInts(): Unit =
    (1 to 32).map(_ * 8).foreach(length => util.writeToFile(pathOf(s"Int$length"), header + intsTemplates(length)))

  def uintsTemplate(length: Int): String = {
    s"""
       |package ethabi
       |package types
       |package generated
       |final class Uint$length(val value: BigInt) extends SolType {
       |  assert(value.bitLength <= $length)
       |  override def toString = value.toString
       |}
       |
       |object Uint$length {
       |  def apply(value: BigInt): Uint$length = new Uint$length(value)
       |  def from(value: String): Uint$length = Uint$length(BigInt(value))
       |
       |  implicit lazy val typeInfo: TypeInfo[Uint$length] = new TypeInfo[Uint$length] {
       |    override def name: String = "uint$length"
       |    override def isStatic: Boolean = true
       |    override def encode[U >: Uint$length](value: U): Array[Byte] = {
       |      UintType.encode(value.asInstanceOf[Uint$length].value)
       |    }
       |    override def decode(bytes: Array[Byte], position: Int): (Uint$length, Int) = {
       |      val value = UintType.decode(bytes, $length, position)
       |      (Uint$length(value), 32)
       |    }
       |  }
       |}
       |""".stripMargin
  }

  def genUints(): Unit =
    (1 to 32).map(_ * 8).foreach(length => util.writeToFile(pathOf(s"Uint$length"), header + uintsTemplate(length)))

  genBytes()
  genInts()
  genUints()
}
