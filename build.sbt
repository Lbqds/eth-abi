import sbtassembly.AssemblyPlugin.defaultShellScript

lazy val scala212 = "2.12.8"
lazy val scala213 = "2.13.1"
lazy val ethAbiVersion = "0.3.0"

def scalacOptionByVersion(version: String): Seq[String] = {
  val optional: Seq[String] =
    if (priorTo213(version)) Seq("-Ypartial-unification")
    else Seq("-Ymacro-annotations")

  Seq(
    "-encoding",
    "utf8",
    "-Xfatal-warnings",
    "-Xlint",
    "-deprecation",
    "-unchecked",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:existentials",
    //"-Xlog-implicits",
    //"-Xlog-implicit-conversions",
    "-language:postfixOps") ++ optional
}

def priorTo213(version: String): Boolean = {
  CrossVersion.partialVersion(version) match {
    case Some((2, minor)) if minor < 13 => true
    case _                              => false
  }
}

val commonSettings = Seq(
  organization := "com.github.lbqds",
  scalaVersion := scala213,
  crossScalaVersions := Seq(scala212, scala213),
  version := ethAbiVersion,
  scalacOptions ++= scalacOptionByVersion(scalaVersion.value),
  test in assembly := {}
)

val macroSettings = Seq(
  libraryDependencies ++= (Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided,
    "org.scala-lang" % "scala-compiler" % scalaVersion.value % Provided
  ) ++ (
    if (priorTo213(scalaVersion.value)) Seq(
      compilerPlugin("org.scalamacros" % "paradise" % "2.1.1").cross(CrossVersion.patch)
    ) else Nil
  ))

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

lazy val root =
  Project(id = "root", base = file("."))
    .settings(commonSettings)
    .settings(macroSettings)
    .settings(name := "root")
    .settings(publishSettings)
    .aggregate(ethabi, codegen, examples)
    .disablePlugins(sbtassembly.AssemblyPlugin)

lazy val ethabi =
  Project(id = "ethabi", base = file("ethabi"))
    .settings(commonSettings)
    .settings(macroSettings)
    .settings(name := "ethabi")
    .settings(Dependencies.deps)
    .settings(publishSettings)
    .settings(addCompilerPlugin("org.typelevel" % "kind-projector" % "0.11.0" cross CrossVersion.full))
    .settings(
      unmanagedSourceDirectories in Compile += {
        CrossVersion.partialVersion(scalaVersion.value) match {
          case Some((2, n)) if n >= 13 => (scalaSource in Compile).value.getParentFile / "scala-2.13+"
          case _                       => (scalaSource in Compile).value.getParentFile / "scala-2.13-"
        }
      }
    )
    .enablePlugins(spray.boilerplate.BoilerplatePlugin)
    .disablePlugins(sbtassembly.AssemblyPlugin)

lazy val codegen =
  Project(id = "codegen", base = file("codegen"))
    .settings(commonSettings)
    .dependsOn(ethabi)
    .settings(Dependencies.codegenDeps)
    .settings(
      name := "codegen",
      assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultShellScript)),
      assemblyJarName := s"abi-codegen-$ethAbiVersion",
      skip in publish := true
    )

lazy val examples =
  Project(id = "examples", base = file("examples"))
    .settings(commonSettings)
    .dependsOn(ethabi)
    .disablePlugins(sbtassembly.AssemblyPlugin)
    .settings(
      name := "examples",
      skip in publish := true
    )
