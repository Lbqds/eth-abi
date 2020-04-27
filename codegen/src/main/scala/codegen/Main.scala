package codegen

import ethabi.util
import scopt.OParser

object Main extends App {
  case class Params(interactive: Boolean = false, abiFile: String = "", binFile: Option[String] = None, packages: String = "", className: String = "", output: String = "")

  val builder = OParser.builder[Params]
  val cmdParser = {
    import builder._
    OParser.sequence(
      programName("abi-codegen"),
      head("abi-codegen", "0.1"),
      opt[Unit]('i', "interactive")
        .optional()
        .action((_, params) => params.copy(interactive = true))
        .text("repl mode"),
      cmd("gen")
        .text("generate scala code from solidity abi and bin code")
        .children(
          opt[String]('a', "abi")
            .required()
            .valueName("<abiFile>")
            .action((value, params) => params.copy(abiFile = value))
            .text("contract abi file"),
          opt[String]('b', "bin")
            .optional()
            .valueName("<binFile>")
            .action((value, params) => params.copy(binFile = Some(value)))
            .text("contract bin file"),
          opt[String]('p', "package")
            .required()
            .valueName("<packages>")
            .action((value, params) => params.copy(packages = value))
            .text("""package name e.g. "examples.token""""),
          opt[String]('c', "className")
            .required()
            .valueName("<className>")
            .action((value, params) => params.copy(className = value))
            .text("class name"),
          opt[String]('o', "output")
            .required()
            .valueName("<output dir>")
            .action((value, params) => params.copy(output = value))
            .text("output directory"),
        ),
      help('h', "help").text("show usage")
    )
  }

  def generate(params: Params): Unit = {
    val header = "// AUTO GENERATED, DO NOT EDIT\n\n"
    val code = Codegen.codeGen(params.abiFile, params.binFile, params.packages.split('.').toList, params.className)
    val path = params.output + "/" + params.packages.split('.').mkString("/")
    val fileName = s"${params.className}.scala"
    util.writeToFile(path, fileName, header + code.syntax)
  }

  OParser.parse(cmdParser, args, Params()) match {
    case Some(params) =>
      if (params.interactive) {
        println("start repl mode, ignore other options")
        Repl.start()
      } else generate(params)
    case None => println("invalid params, please refer to usage")
  }
}
