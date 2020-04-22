package codegen

import scala.io.StdIn
import scala.io.Source
import io.circe.jawn.decode
import io.circe.generic.auto._
import fastparse._
import NoWhitespace._

import scala.annotation.tailrec

// TODO: support decode event, encode struct directly
object Repl {

  class InvalidCommandException(msg: String) extends RuntimeException(msg)

  sealed trait Command {
    type R
    def execute(): R
  }

  // >> load `absolute path of abi file`
  case class Load(path: String) extends Command {
    type R = Seq[AbiDefinition]

    override def execute(): Seq[AbiDefinition] = {
      val file = Source.fromFile(path)
      val content = file.getLines().mkString
      file.close()
      val result = decode[Seq[AbiDefinition]](content).right.get
      println("load completed")
      result
    }

    override def toString: String = s"load $path\n"
  }

  // >> encode `selector` `args`
  // >> encode <ctor> `args`  =>   if is constructor
  case class EncodeArg(selector: String, args: Seq[String], defs: Seq[AbiDefinition]) extends Command {
    type R = String

    override def execute(): String = {
      val abiDef =
        if (selector == "<ctor>") defs.filter(_.isConstructor).head
        else defs.filter(_.isFunction).find(_.name.get == selector).get
      if (abiDef.inputs.get.length != args.length)
        throw new InvalidCommandException(s"encode selector $selector, expect ${abiDef.inputs.get.length} args, but have ${args.length}")
      val types = abiDef.inputs.get.map(_.tpe.toString)
      ???
    }

    override def toString: String = s"encode $selector ${args.mkString(" ")}\n"
  }

  // >> decode `selector` `encoded`
  case class DecodeRet(selector: String, encoded: String, defs: Seq[AbiDefinition]) extends Command {
    type R = String

    override def execute(): String = {
      ???
    }

    override def toString: String = s"decode $selector $encoded\n"
  }

  // >> quit
  case object Quit extends Command {
    type R = Unit

    override def execute(): Unit = System.exit(0)

    override def toString: String = "quit\n"
  }

  private val spaces: Array[Char] = " \t\r\n\f".toCharArray
  private def spaceP[_: P](num: Int) = P(CharsWhileIn(" \t\r\n\f", num))
  private def anyCharExceptSpacesP[_ : P] = P(CharPred(c => !spaces.contains(c)))
  private def absolutePathP[_ : P] = P("/" ~ AnyChar.rep)
  private def ctorP[_ : P] = P("<ctor>")
  private def funcNameP[_ : P] = P(CharIn("a-zA-Z$_") ~ CharIn("a-zA-Z0-9$_").rep)
  private def selectorP[_ : P] = P(ctorP | funcNameP)
  private def stringP[_ : P] = P(anyCharExceptSpacesP.rep)
  private def argsP[_ : P] = P(spaceP(1) ~ stringP.!).rep
  implicit class WithSpace[T](p: => P[T]) {
    def withSpace0[_: P]: P[T] = P(spaceP(0) ~ p ~ spaceP(0))
    def withSpace1[_: P]: P[T] = P(spaceP(1) ~ p ~ spaceP(1))
  }

  def parseCmd(line: String): Command = {
    def loadCmdP[_ : P] = P("load" ~/ spaceP(1) ~ absolutePathP.!).withSpace0.map(Load.apply)
    def encodeArgCmdP[_ : P] = P("encode" ~/ spaceP(0) ~ selectorP.! ~ argsP).withSpace0.map {
      case (selector, args) => EncodeArg(selector, args, Seq.empty[AbiDefinition])
    }
    def decodeRetCmdP[_ : P] = P("decode" ~/ funcNameP.!.withSpace1 ~ stringP.!).withSpace0.map {
      case (selector, encoded) => DecodeRet(selector, encoded, Seq.empty[AbiDefinition])
    }
    def quitCmdP[_ : P] = P("quit" ~/ spaceP(0)).!.withSpace0.map(_ => Quit)
    def cmdP[_ : P] = P(loadCmdP | encodeArgCmdP | decodeRetCmdP | quitCmdP)

    parse(line, cmdP(_)) match {
      case Parsed.Success(cmd, _) => cmd
      case failure: Parsed.Failure => throw new InvalidCommandException(failure.msg)
    }
  }

  def loadType(): Unit = {
    import scala.reflect.runtime.universe
    import ethabi.types.generated._
    import ethabi.util.Hex
    val mirror = universe.runtimeMirror(getClass.getClassLoader)
    val module = mirror.staticModule("ethabi.types.generated.Bytes1")
    val obj = mirror.reflectModule(module)
    val instance = obj.instance.asInstanceOf[Bytes1.type]
    println(Hex.bytes2Hex(instance.typeInfo.encode(Bytes1(Array(1)))))
  }

  def start(): Unit = {
    var ctx = Seq.empty[AbiDefinition]

    @tailrec
    def loop(): Unit = {
      print(">>")
      val line = StdIn.readLine()
      if (line == null) Quit.execute()
      parseCmd(line) match {
        case cmd: Load => ctx = cmd.execute()
        case cmd: EncodeArg => println(cmd.copy(defs = ctx).execute())
        case cmd: DecodeRet => println(cmd.copy(defs = ctx).execute())
        case cmd => cmd.execute()
      }
      loop()
    }
    loop()
  }
}
