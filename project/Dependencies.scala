import sbt._
import Keys._

object Dependencies {
  val circeVersion = "0.13.0"
  val akkaVersion = "2.6.0"
  val scalaMetaVersion = "4.3.10"

  val scalactic = "org.scalactic" %% "scalactic" % "3.1.1"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1" % "test"
  val scalaMeta = "org.scalameta" %% "scalameta" % scalaMetaVersion
  val fastParser = "com.lihaoyi" %% "fastparse" % "2.3.0"
  val scopt = "com.github.scopt" %% "scopt" % "4.0.0-RC2"
  val circeCore = "io.circe" %% "circe-core" % circeVersion
  val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
  val circeParser = "io.circe" %% "circe-parser" % circeVersion
  val scrypto = "org.scorexfoundation" %% "scrypto" % "2.1.8"
  val ammTerminal = "com.lihaoyi" %% "ammonite-terminal" % "2.1.0"
  val fansi = "com.lihaoyi" %% "fansi" % "0.2.9"
  val httpClient = "org.http4s" %% "http4s-jdk-http-client" % "0.3.0"
  val http4sCirce = "org.http4s" %% "http4s-circe" % "0.21.3"
  val catsCore = "org.typelevel" %% "cats-core" % "2.1.1"
  val catsEffect = "org.typelevel" %% "cats-effect" % "2.1.3"
  val catsRetry = "com.github.cb372" %% "cats-retry" % "1.1.1"

  val l = libraryDependencies
  val deps = l ++= Seq(catsCore, catsEffect, httpClient, http4sCirce, catsRetry, scalaTest, scrypto, circeCore, circeGeneric, circeParser)
  val codegenDeps = l ++= Seq(scalaMeta, circeCore, circeGeneric, circeParser, fastParser, scopt,
    fansi, ammTerminal, scalaTest)
}
