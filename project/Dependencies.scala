import sbt._

object Dependencies {

  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.17" % Test

  object Cats {
    private val cats = "2.12.0"
    val core         = "org.typelevel" %% "cats-core" % cats
    val laws         = "org.typelevel" %% "cats-laws" % cats
    val all          = Seq(core, laws)
  }

  object Plugins {
    val organizeImports = "com.github.liancheng" %% "organize-imports" % "0.6.0"
  }

  val logging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"

  val testing = Seq(scalaTest)
  val core    = Cats.all

  val all = testing ++ core
}
