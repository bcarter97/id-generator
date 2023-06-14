lazy val scala3                 = "3.3.0"
lazy val scala213               = "2.13.10"
lazy val scala212               = "2.12.17"
lazy val supportedScalaVersions = List(scala3, scala213, scala212)

name        := "id-generator"
description := "Generate reproducible UUIDs based of a sequence of numbers, with the possibility of generating sub UUIDs from the parent UUID."

semanticdbEnabled  := true
semanticdbVersion  := scalafixSemanticdb.revision
scalaVersion       := scala213
crossScalaVersions := supportedScalaVersions

Global / onChangedBuildSource := ReloadOnSourceChanges
Global / scalafmtOnCompile    := true

ThisBuild / scalafixDependencies += Dependencies.Plugins.organizeImports
ThisBuild / organization           := "io.github.bcarter97"
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / homepage               := Some(url("https://github.com/bcarter97/id-generator"))
ThisBuild / licenses               := List("BSD New" -> url("https://opensource.org/licenses/BSD-3-Clause"))
ThisBuild / developers             := List(
  Developer(
    "bcarter97",
    "Ben Carter",
    "ben@carter.gg",
    url("https://github.com/bcarter97/")
  )
)
ThisBuild / versionScheme          := Some("early-semver")

Compile / doc / scalacOptions += "-no-link-warnings"

libraryDependencies ++= Dependencies.all

// Formatting aliases
addCommandAlias("checkFix", "scalafixAll --check OrganizeImports; scalafixAll --check")
addCommandAlias("runFix", "scalafixAll OrganizeImports; scalafixAll")
addCommandAlias("checkFmt", "scalafmtCheckAll; scalafmtSbtCheck")
addCommandAlias("runFmt", "scalafmtAll; scalafmtSbt")

// CI aliases
addCommandAlias("ciBuild", "clean; checkFix; checkFmt; +test")
addCommandAlias("ciCodeCov", "coverage; test; coverageReport")
