import xerial.sbt.Sonatype._

// To sync with Maven central, you need to supply the following information:
licenses += ("BSD New", url("https://opensource.org/licenses/BSD-3-Clause"))
sonatypeProfileName    := "io.github.bcarter97"
sonatypeCredentialHost := "s01.oss.sonatype.org"
publishMavenStyle      := true
sonatypeProjectHosting := Some(GitHubHosting("bcarter97", "id-generator", "ben@carter.gg"))
pomIncludeRepository   := { _ => false }
publishTo              := sonatypePublishToBundle.value
versionScheme          := Some("early-semver")
