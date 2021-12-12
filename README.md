# id-generator

[![Scala CI](https://github.com/bcarter97/id-generator/actions/workflows/scala.yml/badge.svg)](https://github.com/bcarter97/id-generator/actions/workflows/scala.yml)
[![CI Release](https://github.com/bcarter97/id-generator/actions/workflows/release.yml/badge.svg)](https://github.com/bcarter97/id-generator/actions/workflows/release.yml)
[![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/io.github.bcarter97/id-generator_3?server=https%3A%2F%2Fs01.oss.sonatype.org)](https://s01.oss.sonatype.org/content/repositories/releases/io/github/bcarter97/)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/io.github.bcarter97/id-generator_3?server=https%3A%2F%2Fs01.oss.sonatype.org)](https://s01.oss.sonatype.org/content/repositories/snapshots/io/github/bcarter97/)

Generate reproducable UUIDs based of a sequence of numbers, with the possibility of generating sub UUIDs from the parent UUID.

## Usage

In your `build.sbt` add:

```scala
libraryDependencies += "io.github.bcarter97" %% "id-generator" % "x.x.x"
```

Then you can import the `Generator` with:

```scala
import io.github.bcarter97.Generator

object main extends App {
  val generator = Generator()
  val id = generator.id(5)
  print(id)
  // e4da3b7f-bbce-3345-9777-2b0674a318d5
}
```
