package codegen

import java.io.{File, PrintWriter}

import scopt.OParser

object Main extends App {
  def writeToFile(path: String, fileName: String, code: String): Unit = {
    val dir = new File(path)
    dir.mkdirs
    new PrintWriter(path + s"/$fileName") {
      write(code)
      close()
    }
  }

  case class Params(abiFile: String = "", binFile: Option[String] = None, packages: String = "", className: String = "", output: String = "")

  val builder = OParser.builder[Params]
  val cmdParser = {
    import builder._
    OParser.sequence(
      programName("abi-codegen"),
      head("abi-codegen", "0.1"),
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
      help('h', "help").text("show usage")
    )
  }

  OParser.parse(cmdParser, args, Params()) match {
    case Some(params) =>
      val header = "// AUTO GENERATED, DO NOT EDIT\n\n"
      val code = Codegen.codeGen(params.abiFile, params.binFile, params.packages.split('.').toList, params.className)
      val path = params.output + "/" + params.packages.split('.').mkString("/")
      val fileName = s"${params.className}.scala"
      writeToFile(path, fileName, header + code.syntax)
    case None => println("invalid params, please refer to usage")
  }
}
