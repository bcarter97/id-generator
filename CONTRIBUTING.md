# Contributing

## Pre-reqs

1. Use the [OSS Guide](https://central.sonatype.org/publish/publish-guide/) to create a Jira account, and register your new project.
2. [Generate a GPG key](https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html#step+1%3A+PGP+Signatures) and distribute your public key. Use `gpg full-generate-key` with RSA and RSA and a 4096 bit key instead of what the guide suggests.

> 💡 If you use any other GPG keys locally, you may have to [set the default](https://unix.stackexchange.com/a/339083) for sbt-sonatype to pick it up.

## Releasing

Releasing is done using [`sbt-ci-release`](https://github.com/sbt/sbt-ci-release). When a pull request is merged to master, `sbt-ci-release` will create a snapshot version on Sonatype. To create a new release, run:

```bash
git tag -a v$VERSION -m "v$VERSION"
git push origin v$VERSION
```

> 💡 The tag **must** start with a `v` followed by your versioning schema, e.g. `v1.0.0`

This will trigger the [`release.yml`](./.github/workflows/release.yml) workflow, which will push the tagged artifact to Sonatype.
