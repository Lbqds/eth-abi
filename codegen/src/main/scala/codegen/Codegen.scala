package codegen

import io.circe.jawn.decode
import io.circe.generic.auto._
import scala.meta._
import scala.io.Source

object Codegen {

  private def genFunctions(abiDefinitions: Seq[AbiDefinition]): List[Stat] =
    abiDefinitions.filter(_.isFunction).map(_.genFunction).toList

  private def genCtor(abiDefinitions: Seq[AbiDefinition]): List[Stat] =
    abiDefinitions.filter(_.isConstructor).map(_.genConstructor).toList

  private def genEvent(abiDefinitions: Seq[AbiDefinition]): List[Stat] =
    abiDefinitions.filter(_.isEvent).flatMap(_.genEvent).toList

  private[codegen] def codeGen(abiFile: String, binFile: Option[String], packages: List[String], className: String): Pkg = {

    val abiFileHandler = Source.fromFile(abiFile)
    val abiDefs = decode[Seq[AbiDefinition]](abiFileHandler.getLines.mkString).getOrElse(throw new RuntimeException("invalid abi format"))
    val binFileHandler = binFile.map(f => Source.fromFile(f))
    val binCodeStr = binFileHandler.map(_.getLines.mkString).getOrElse("")

    abiFileHandler.close()
    binFileHandler.foreach(_.close())

    val contents = genFunctions(abiDefs) ::: genCtor(abiDefs) ::: genEvent(abiDefs)
    val selector: (Term.Ref, Term.Name) => Term.Ref = (p, c) => Term.Select(p, c)
    val packagesDef = packages.map(pkg => Term.Name(pkg)).reduceLeft(selector)

    val typeName = Type.Name(className)
    val termName = Term.Name(className)

    q"""
        package $packagesDef {
          import ethabi.util._
          import ethabi.types._
          import ethabi.types.generated._
          import ethabi.protocol._
          import ethabi.protocol.Request._
          import ethabi.protocol.Response.Log
          import ethabi.protocol.Subscription.SubscriptionResult
          import cats.implicits._
          import cats.Applicative
          import cats.effect._
          import cats.effect.concurrent._

          final class $typeName[F[_]: ConcurrentEffect: Timer] private (private val impl: Contract[F]) { self =>
            private val binary = ${Lit.String(binCodeStr)}

            def client: F[Client[F]] = impl.client
            def subscriber: F[Subscriber[F]] = impl.subscriber
            def isDeployed: F[Boolean] = impl.isDeployed
            def address: F[Option[Address]] = impl.address
            def loadFrom(address: Address): F[Unit] = impl.load(address)
            ..$contents
          }

          object $termName {
            def apply[F[_]: ConcurrentEffect: Timer](endpoint: String)(implicit CS: ContextShift[F]): Resource[F, $typeName[F]] = {
              Contract[F](endpoint).flatMap(impl => Resource.liftF(Applicative[F].pure(new $typeName(impl))))
            }
          }
        }
     """
  }
}
