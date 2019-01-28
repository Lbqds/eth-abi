import sbt._
import Keys._

object Dependencies {
  val circeVersion = "0.11.0"
  val akkaVersion = "2.5.19"
  val scalaMetaVersion = "4.1.0"

  val scalactic = "org.scalactic" %% "scalactic" % "3.0.5"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  val scalaMeta = "org.scalameta" %% "scalameta" % scalaMetaVersion
  val fastParser = "com.lihaoyi" %% "fastparse" % "2.1.0"
  val actor = "com.typesafe.akka" %% "akka-actor" % akkaVersion
  val stream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  val akkaHttp = "com.typesafe.akka" %% "akka-http"   % "10.1.7"
  val scopt = "com.github.scopt" %% "scopt" % "4.0.0-RC2"
  val circeCore = "io.circe" %% "circe-core" % circeVersion
  val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
  val circeParser = "io.circe" %% "circe-parser" % circeVersion
  val scrypto = "org.scorexfoundation" %% "scrypto" % "2.0.0"

  val l = libraryDependencies
  val deps = l ++= Seq(scalaTest, actor, stream, scrypto, akkaHttp, circeCore, circeGeneric, circeParser)
  val codegenDeps = l ++= Seq(scalaMeta, circeCore, circeGeneric, circeParser, fastParser, scopt, scalaTest)
  val examplesDpes = l ++= Seq(actor, stream, scalaTest)
}
