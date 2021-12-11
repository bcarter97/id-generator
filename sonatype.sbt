import xerial.sbt.Sonatype._

// To sync with Maven central, you need to supply the following information:
ThisBuild / licenses += ("BSD New", url("https://opensource.org/licenses/BSD-3-Clause"))
ThisBuild / sonatypeProfileName    := "io.github.bcarter97"
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
ThisBuild / publishMavenStyle      := true
ThisBuild / sonatypeProjectHosting := Some(GitHubHosting("bcarter97", "id-generator", "ben@carter.gg"))
ThisBuild / pomIncludeRepository   := { _ => false }
ThisBuild / publishTo              := sonatypePublishToBundle.value
ThisBuild / versionScheme          := Some("early-semver")
