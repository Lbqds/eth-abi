lazy val scala212 = "2.12.8"
lazy val scala213 = "2.13.1"

val commonSettings = Seq(
  organization := "com.github.lbqds",
  crossScalaVersions := Seq(scala212, scala213),
  version := "0.2.0",
  scalacOptions ++= Seq(
    "-encoding", "utf8",
//    "-Xfatal-warnings",
    "-deprecation",
//    "-unchecked",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:existentials",
//    "-Xlog-implicits",
//    "-Xlog-implicit-conversions",
    "-language:postfixOps"),
  test in assembly := {}
)

import xerial.sbt.Sonatype._
val publishSettings = Seq(
  publishTo := sonatypePublishTo.value,
  sonatypeProfileName := "com.github.lbqds",
  publishMavenStyle := true,
  licenses := Seq("GPL" -> url("https://www.gnu.org/licenses/gpl-3.0.txt")),
  sonatypeProjectHosting := Some(GitHubHosting("lbqds", "eth-abi", "lbqds@outlook.com")),
  homepage := Some(url("https://github.com/lbqds")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/lbqds/eth-abi"),
      "scm:git@github.com:lbqds/eth-abi.git"
    )
  ),
  developers := List(
    Developer(id = "lbqds", name = "lbqds", email = "lbqds@outlook.com", url = new URL("https://github.com/lbqds"))
  ),
  publishConfiguration := publishConfiguration.value.withOverwrite(true),
  publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
)

lazy val ethabi =
  Project(id = "eth-abi", base = file("."))
    .settings(commonSettings)
    .settings(name := "eth-abi")
    .settings(Dependencies.deps)
    .settings(publishSettings)
    .enablePlugins(spray.boilerplate.BoilerplatePlugin)
    .disablePlugins(sbtassembly.AssemblyPlugin)

lazy val codegen =
  Project(id = "codegen", base = file("codegen"))
    .settings(commonSettings)
    .dependsOn(ethabi)
    .settings(Dependencies.codegenDeps)
    .settings(
      name := "codegen",
      assemblyJarName := "abi-codegen.jar",
      skip in publish := true
    )

lazy val example =
  Project(id = "examples", base = file("examples"))
    .settings(commonSettings)
    .settings(Dependencies.examplesDpes)
    .dependsOn(ethabi)
    .disablePlugins(sbtassembly.AssemblyPlugin)
    .settings(
      name := "examples",
      skip in publish := true
    )
