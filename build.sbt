import Dependencies.all

lazy val scala3                 = "3.1.0"
lazy val scala213               = "2.13.7"
lazy val scala212               = "2.12.10"
lazy val supportedScalaVersions = List(scala3, scala213, scala212)

name        := "id-generator"
description := "A library for generating reproducible UUIDs"

semanticdbEnabled  := true
semanticdbVersion  := scalafixSemanticdb.revision
scalaVersion       := scala3
crossScalaVersions := supportedScalaVersions

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"

inThisBuild(
  List(
    organization           := "io.github.bcarter97",
    sonatypeCredentialHost := "s01.oss.sonatype.org",
    homepage               := Some(url("https://github.com/bcarter97/id-generator")),
    licenses               := List("BSD New" -> url("https://opensource.org/licenses/BSD-3-Clause")),
    developers             := List(
      Developer(
        "bcarter97",
        "Ben Carter",
        "ben@carter.gg",
        url("https://github.com/bcarter97/")
      )
    )
  )
)

Global / onChangedBuildSource := ReloadOnSourceChanges

libraryDependencies ++= all

// Formatting aliases
addCommandAlias("checkFix", "scalafixAll --check OrganizeImports; scalafixAll --check")
addCommandAlias("runFix", "scalafixAll OrganizeImports; scalafixAll")
addCommandAlias("checkFmt", "scalafmtCheckAll; scalafmtSbtCheck")
addCommandAlias("runFmt", "scalafmtAll; scalafmtSbt")

// CI aliases
addCommandAlias("ciBuild", "checkFix; checkFmt; +test")
