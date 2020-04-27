package codegen

import scala.io.Source
import io.circe.jawn.decode
import io.circe.generic.auto._
import scala.meta._

object Codegen {
  private def genImpl: List[Stat] = List(
    q"private val impl = Contract(endpoint)",
    q"import impl.dispatcher"
  )
  private def genBinary(code: String): List[Stat] = List(q"""private val binary = ${Lit.String(code)}""")
  private def genFunctions(abiDefinitions: Seq[AbiDefinition]): List[Stat] =
    abiDefinitions.filter(_.isFunction).map(_.genFunction).toList
  private def genCtor(abiDefinitions: Seq[AbiDefinition]): List[Stat] =
    abiDefinitions.filter(_.isConstructor).map(_.genConstructor).toList
  private def genEvent(abiDefinitions: Seq[AbiDefinition]): List[Stat] =
    abiDefinitions.filter(_.isEvent).flatMap(_.genEvent).toList
  private def genSupMethods: List[Stat] = {
    val isDeployed = q"def isDeployed: Boolean = impl.isDeployed"
    val contractAddress = q"def contractAddress: Address = impl.address.get"
    val loadFrom = q"def loadFrom(contractAddress: Address) = impl.load(contractAddress)"
    List(isDeployed, contractAddress, loadFrom)
  }

  private def stats(abiFile: String, binFile: Option[String]) = {
    val source = Source.fromFile(abiFile).getLines().mkString
    val abiDefs = decode[Seq[AbiDefinition]](source).right.get
    val code = binFile.map(f => Source.fromFile(f).getLines().mkString)
    genImpl ::: genBinary(code.getOrElse("")) ::: genFunctions(abiDefs) ::: genCtor(abiDefs) ::: genEvent(abiDefs) ::: genSupMethods
  }

  def codeGen(abiFile: String, binFile: Option[String], packages: List[String], className: String): Pkg  = {
    val contents = stats(abiFile, binFile)
    val template = Template(List.empty, List.empty, Self(Term.Name("self"), None), contents)
    val primary = Ctor.Primary(List.empty, Term.Name(className), List(List(Term.Param(List.empty, Term.Name("endpoint"), Some(Type.Name("String")), None))))
    val classDef = Defn.Class(List(Mod.Final()), Type.Name(className), List.empty, primary, template)
    val selector: (Term.Ref, Term.Name) => Term.Ref = (p, c) => Term.Select(p, c)
    val packagesDef = packages.map(pkg => Term.Name(pkg)).reduceLeft(selector)

    q"""
        package $packagesDef {
          import akka.NotUsed
          import akka.stream.scaladsl.Source
          import ethabi.util.{Hex, Hash}
          import ethabi.types._
          import ethabi.types.generated._
          import ethabi.protocol.{Contract, EventValue}
          import ethabi.protocol.Request._
          import ethabi.protocol.Response.Log
          import scala.concurrent.Future

          $classDef
        }
     """
  }
}
