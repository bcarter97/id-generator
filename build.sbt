import Dependencies.all

lazy val scala3                 = "3.1.0"
lazy val scala213               = "2.13.7"
lazy val scala212               = "2.12.10"
lazy val supportedScalaVersions = List(scala3, scala213, scala212)

organization := "io.github.bcarter97"
name         := "id-generator"
description  := "A library for generating reproducible UUIDs"

ThisBuild / semanticdbEnabled                              := true
ThisBuild / semanticdbVersion                              := scalafixSemanticdb.revision
ThisBuild / scalaVersion                                   := scala3
ThisBuild / crossScalaVersions                             := supportedScalaVersions
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"

Global / onChangedBuildSource := ReloadOnSourceChanges

libraryDependencies ++= all

// Formatting aliases
addCommandAlias("checkFix", "scalafixAll --check OrganizeImports; scalafixAll --check")
addCommandAlias("runFix", "scalafixAll OrganizeImports; scalafixAll")
addCommandAlias("checkFmt", "scalafmtCheckAll; scalafmtSbtCheck")
addCommandAlias("runFmt", "scalafmtAll; scalafmtSbt")

// CI aliases
addCommandAlias(s"ci", "checkFix; checkFmt; +test")
