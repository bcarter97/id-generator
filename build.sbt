import Dependencies.all

scalaVersion      := "3.1.0"
organization      := "io.github.bcarter97"
name              := "id-generator"
semanticdbEnabled := true
semanticdbVersion := scalafixSemanticdb.revision

licenses += ("BSD New", url("https://opensource.org/licenses/BSD-3-Clause"))

Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"

libraryDependencies ++= all

addCommandAlias("checkFix", "scalafixAll --check OrganizeImports; scalafixAll --check")
addCommandAlias("runFix", "scalafixAll OrganizeImports; scalafixAll")
addCommandAlias("checkFmt", "scalafmtCheckAll; scalafmtSbtCheck")
addCommandAlias("runFmt", "scalafmtAll; scalafmtSbt")
