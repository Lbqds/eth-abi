package codegen

import scala.io.StdIn
import scala.io.Source
import scala.reflect.runtime
import scala.annotation.tailrec
import scala.util.control.NonFatal
import io.circe.jawn.decode
import io.circe.generic.auto._
import fastparse._
import NoWhitespace._
import ethabi.types._
import ethabi.util.Hex
import ammonite.terminal._
import ammonite.terminal.filters._
import java.io.InputStreamReader
import java.io.OutputStreamWriter

// TODO: support decode event, encode struct directly
object Repl {

  private val ctorSelector = "<ctor>"
  private val mirror = runtime.universe.runtimeMirror(getClass.getClassLoader)
  import runtime.universe.TermName

  final class InvalidCommandException(msg: String) extends RuntimeException(msg)

  sealed trait Command {
    type R
    def execute(): R
  }

  // >> load `absolute path of abi file`
  final case class Load(path: String) extends Command {
    type R = Seq[AbiDefinition]

    override def execute(): Seq[AbiDefinition] = {
      val file = Source.fromFile(path)
      val content = file.getLines().mkString
      file.close()
      val result = decode[Seq[AbiDefinition]](content).getOrElse(throw new RuntimeException("invalid abi format"))
      println("load completed")
      result
    }

    override def toString: String = s"load $path\n"
  }

  // >> encode `selector` `args`
  // >> encode <ctor> `args`  =>   if is constructor
  final case class EncodeArg(selector: String, args: Seq[String], defs: Seq[AbiDefinition]) extends Command {
    type R = String

    override def execute(): String = {
      val abiDef =
        if (selector == ctorSelector) defs.filter(_.isConstructor).head
        else defs.filter(_.isFunction).find(_.name.get == selector).get
      // TODO: support tuple type(components)
      if (abiDef.inputs.get.exists(_.isTupleTpe)) throw new InvalidCommandException("tuple type unsupported now")
      if (abiDef.inputs.get.length != args.length)
        throw new InvalidCommandException(s"encode selector $selector, expect ${abiDef.inputs.get.length} args, but have ${args.length}")
      if (abiDef.inputs.get.exists(_.components.isDefined)) throw new InvalidCommandException("tuple type unsupported now")
      val types = abiDef.inputs.get.map(_.tpe.toString)
      val (results, encoders) = types.zip(args).map(p => encoder(p._1, Some(p._2))).map(p => (p._2.encode(p._1.get), p._2)).unzip
      Hex.bytes2Hex(TupleType.encode(encoders, results))
    }

    override def toString: String = s"encode $selector ${args.mkString(" ")}\n"
  }

  // >> decode `selector` `encoded`
  final case class DecodeRet(selector: String, encoded: String, defs: Seq[AbiDefinition]) extends Command {
    type R = String

    override def execute(): String = {
      val abiDef =
        if (selector == ctorSelector) throw new InvalidCommandException("ctor have outputs")
        else defs.filter(_.isFunction).find(_.name.get == selector).get
      if (abiDef.outputs.get.exists(_.isTupleTpe)) throw new InvalidCommandException("tuple type unsupported now")
      val types = abiDef.outputs.get.map(_.tpe.toString)
      def helper[T]: Option[T] = None
      val (_, encoders) = types.map(t => (t, helper[String])).map((encoder _).tupled).unzip
      val (results, _) = TupleType.decode(Hex.hex2Bytes(encoded), 0, encoders)
      encoders.zip(results).map(p => Hex.bytes2Hex(p._1.encode(p._2))).mkString("(", ", ", ")")
    }

    override def toString: String = s"decode $selector $encoded\n"
  }

  // >> quit
  case object Quit extends Command {
    type R = Unit
    override def execute(): Unit = System.exit(0)
    override def toString: String = "quit\n"
  }

  // ignore whitespaces
  case object Empty extends Command {
    type R = Unit
    override def execute(): Unit = {}
    override def toString: String = "\n"
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
    def emptyP[_ : P] = P(CharsWhileIn(" \t\r\n\f") ~/ spaceP(0)).map(_ => Empty)
    def cmdP[_ : P] = P(loadCmdP | encodeArgCmdP | decodeRetCmdP | quitCmdP | emptyP)

    if (line == "") Empty
    else parse(line, cmdP(_)) match {
      case Parsed.Success(cmd, _) => cmd
      case failure: Parsed.Failure => throw new InvalidCommandException(failure.msg)
    }
  }

  def encoder(tpe: String, arg: Option[String]): (Option[SolType], TypeInfo[SolType]) = {
    val isGenerated = tpe.startsWith("Int") || tpe.startsWith("Uint") || tpe.startsWith("Bytes")
    val modulePath = if (isGenerated) s"ethabi.types.generated.$tpe" else s"ethabi.types.$tpe"
    val module = mirror.staticModule(modulePath)
    val instance = mirror.reflect(mirror.reflectModule(module).instance)
    val method = module.info.member(TermName("from")).asMethod
    val result = arg.map(instance.reflectMethod(method)(_).asInstanceOf[SolType])
    // force eval lazy
    val encoderMethod = module.info.member(TermName("typeInfo")).asMethod
    val typeInfo = instance.reflectMethod(encoderMethod)().asInstanceOf[TypeInfo[SolType]]
    (result, typeInfo)
  }

  /*
  def start(): Unit = {
    var ctx = Seq.empty[AbiDefinition]

    @tailrec
    def loop(): Unit = {
      print(">>")
      try {
        val line = StdIn.readLine()
        if (line == null) Quit.execute()
        parseCmd(line) match {
          case cmd: Load => ctx = cmd.execute()
          case cmd: EncodeArg => println(cmd.copy(defs = ctx).execute())
          case cmd: DecodeRet => println(cmd.copy(defs = ctx).execute())
          case cmd => cmd.execute()
        }
      } catch {
        case NonFatal(exp) => println(exp)
        case e: Throwable => throw e
      }
      loop()
    }
    loop()
  }
   */
  def start(): Unit = {
    @tailrec
    def loop(): Unit = {
      Terminal.readLine(
        ">>",
        new InputStreamReader(System.in),
        new OutputStreamWriter(System.out),
        Filter.merge(BasicFilters.all)
      ) match {
        case Some(line) => println(line)
        case _ => println("none")
      }
      loop()
    }
    loop()
  }


  /*
  System.setProperty("ammonite-sbt-build", "true")
    var history = List.empty[String]
    val selection = GUILikeFilters.SelectionFilter(indent = 4)
    def multilineFilter: Filter = Filter.partial{
      case TermState(13 ~: rest, b, c, _) if b.count(_ == '(') != b.count(_ == ')') =>
        BasicFilters.injectNewLine(b, c, rest)
    }
    val reader = new java.io.InputStreamReader(System.in)
    val cutPaste = ReadlineFilters.CutPasteFilter()
    rec()
    @tailrec def rec(): Unit = {
      val historyFilter = new HistoryFilter(() => history.toVector, fansi.Color.Blue)
      Terminal.readLine(
        Console.MAGENTA + (0 until 10).mkString + "\n@@@ " + Console.RESET,
        reader,
        new OutputStreamWriter(System.out),
        Filter.merge(
          UndoFilter(),
          cutPaste,
          historyFilter,
          multilineFilter,
          selection,
          BasicFilters.tabFilter(4),
          GUILikeFilters.altFilter,
          GUILikeFilters.fnFilter,
          ReadlineFilters.navFilter,
  //        Example multiline support by intercepting Enter key
          BasicFilters.all
        ),
        // Example displayTransform: underline all non-spaces
        displayTransform = (buffer, cursor) => {
          // underline all non-blank lines

          def hl(b: Vector[Char]): Vector[Char] = b.flatMap{
            case ' ' => " "
            case '\n' => "\n"
            case c => Console.UNDERLINED + c + Console.RESET
          }
          // and highlight the selection
          val ansiBuffer = fansi.Str(hl(buffer))
          val (newBuffer, cursorOffset) = SelectionFilter.mangleBuffer(
            selection, ansiBuffer, cursor, fansi.Reversed.On
          )
          val newNewBuffer = HistoryFilter.mangleBuffer(
            historyFilter, newBuffer, cursor,
            fansi.Color.Green
          )

          (newNewBuffer, cursorOffset)
        }
      ) match {
        case None => println("Bye!")
        case Some(s) =>
          history = s :: history
          println(s)
          rec()
      }
    }
   */
}
