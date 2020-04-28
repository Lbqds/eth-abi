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
  val actor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  val stream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  val akkaHttp = "com.typesafe.akka" %% "akka-http"   % "10.1.11"
  val scopt = "com.github.scopt" %% "scopt" % "4.0.0-RC2"
  val circeCore = "io.circe" %% "circe-core" % circeVersion
  val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
  val circeParser = "io.circe" %% "circe-parser" % circeVersion
  val scrypto = "org.scorexfoundation" %% "scrypto" % "2.1.8"
  val ammTerminal = "com.lihaoyi" %% "ammonite-terminal" % "2.1.0"
  val fansi = "com.lihaoyi" %% "fansi" % "0.2.9"

  val l = libraryDependencies
  val deps = l ++= Seq(scalaTest, actor, stream, scrypto, akkaHttp, circeCore, circeGeneric, circeParser)
  val codegenDeps = l ++= Seq(scalaMeta, circeCore, circeGeneric, circeParser, fastParser, scopt,
    fansi, ammTerminal, scalaTest)
  val examplesDpes = l ++= Seq(actor, stream, scalaTest)
}
