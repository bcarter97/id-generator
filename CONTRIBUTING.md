# Releasing to Sonatype

## Pre-reqs

1. Use the [OSS Guide](https://central.sonatype.org/publish/publish-guide/) to create a Jira account, and register your new project.
2. [Generate a GPG key](https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html#step+1%3A+PGP+Signatures) and distribute your public key. Use `gpg full-generate-key` with RSA and RSA and a 4096 bit key instead of what the guide suggests.
3. [Add your sbt credentials](https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html#step+3%3A+Credentials) locally. This is the same credentials as your Jira account.

> 💡 If you use any other GPG keys locally, you may have to [set the default](https://unix.stackexchange.com/a/339083) for sbt-sonatype to pick it up.

## Releasing

Releasing is currently done semi-automatically. Run `sbt release` and follow the prompts to set the version, next version, and push to VC. The release steps are automatically configured to push to sonatype as part of the release.

## Notes

- sbt build keys need to match (for example, don't use ThisBuild in `sonatype.sbt` else the release steps won't find it).
