import sbt._

object Dependencies {

  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.10" % "it,test"

  object Cats {
    private val cats = "2.7.0"
    val core         = "org.typelevel" %% "cats-core" % cats
    val laws         = "org.typelevel" %% "cats-laws" % cats
    val all          = Seq(core, laws)
  }

  val logging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"

  val testing = Seq(scalaTest)
  val core    = Cats.all

  val all = testing ++ core
}
