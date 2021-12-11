import Dependencies.all
import ReleaseTransformations._

lazy val scala3                 = "3.1.0"
lazy val scala213               = "2.13.7"
lazy val scala212               = "2.12.10"
lazy val supportedScalaVersions = List(scala3, scala213, scala212)

organization := "io.github.bcarter97"
name         := "id-generator"
description  := "A library for generating reproducible UUIDs"

semanticdbEnabled  := true
semanticdbVersion  := scalafixSemanticdb.revision
scalaVersion       := scala3
crossScalaVersions := supportedScalaVersions
releaseCrossBuild  := true // true if you cross-build the project for multiple Scala versions

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"

releaseProcess                := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  releaseStepCommandAndRemaining("+test"),
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

Global / onChangedBuildSource := ReloadOnSourceChanges

libraryDependencies ++= all

// Formatting aliases
addCommandAlias("checkFix", "scalafixAll --check OrganizeImports; scalafixAll --check")
addCommandAlias("runFix", "scalafixAll OrganizeImports; scalafixAll")
addCommandAlias("checkFmt", "scalafmtCheckAll; scalafmtSbtCheck")
addCommandAlias("runFmt", "scalafmtAll; scalafmtSbt")

// CI aliases
addCommandAlias(s"ci", "checkFix; checkFmt; +test")
