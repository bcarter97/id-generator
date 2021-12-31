# id-generator

[![CI Build](https://github.com/bcarter97/id-generator/actions/workflows/scala.yml/badge.svg)](https://github.com/bcarter97/id-generator/actions/workflows/scala.yml)
[![CI Release](https://github.com/bcarter97/id-generator/actions/workflows/release.yml/badge.svg)](https://github.com/bcarter97/id-generator/actions/workflows/release.yml)
[![codecov](https://codecov.io/gh/bcarter97/id-generator/branch/main/graph/badge.svg?token=J3A2WGYUJK)](https://codecov.io/gh/bcarter97/id-generator)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

[![Maven metadata URL](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fio%2Fgithub%2Fbcarter97%2Fid-generator_3%2Fmaven-metadata.xml)](https://mvnrepository.com/artifact/io.github.bcarter97/id-generator)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/io.github.bcarter97/id-generator_3?label=snapshot&server=https%3A%2F%2Fs01.oss.sonatype.org)](https://s01.oss.sonatype.org/content/repositories/snapshots/io/github/bcarter97/id-generator_3/)

Generate reproducible UUIDs based of a sequence of numbers, with the possibility of generating sub UUIDs from the parent UUID.

## Usage

In your `build.sbt` add:

```scala
libraryDependencies += "io.github.bcarter97" %% "id-generator" % "x.x.x"
```

Then you can use the `Generator` like so:

```scala
import io.github.bcarter97.Generator

val generator = Generator()

generator.id(5)
// e4da3b7f-bbce-3345-9777-2b0674a318d5
```

### Generating primary UUIDs

As well as using `generator.id(n)` as above, there are a number of ways to generate a UUID.

To sample a random ID (starting from the first index), use `sample`. This will automatically increment the sample index until the specified `maxIndex` is reached, at which point it will loop back around.

```scala
val generator = Generator(2)

val id1 = generator.sample()
// c4ca4238-a0b9-3382-8dcc-509a6f75849b

val id2 = generator.sample()
// c81e728d-9d4c-3f63-af06-7f89cc14862c

val id3 = generator.sample()
// c4ca4238-a0b9-3382-8dcc-509a6f75849b <-- same as id1
```

> 💡 Using `id()` without an index will call the underlying `sample` method.

You can sample a range of UUIDs by passing a number to `sample`:

```scala
val ids = generator.sample(2)
// Vector(c4ca4238-a0b9-3382-8dcc-509a6f75849b, c81e728d-9d4c-3f63-af06-7f89cc14862c)
```

Similarly, you can specify the range of UUIDs to generate, to make the list reproducible:

```scala
val ids = generator.ids(5, 8)
// Vector(e4da3b7f-bbce-3345-9777-2b0674a318d5, 1679091c-5a88-3faf-afb5-e6087eb1b2dc, 8f14e45f-ceea-367a-9a36-dedd4bea2543, c9f0f895-fb98-3b91-99f5-1fd0297e236d)
```

### Generating sub UUIDs

Sub UUIDs are UUIDs derived from a `parentId`. Each `parentId` has a random, reproducible number of sub UUIDs (the amount of which is specified by `subIds` parameter). Any generated `subId` can be reversed to find the `parentId` that generated it.

To generate a list of `subIds` from a `parentId`:

```scala
val id = generator.sample()

val subIds = generator.subIdsFromId(id)
// List(1c72c43a-c2dc-3fc7-9f59-c606b09ce54a, 8884429d-c771-3fcc-a603-fcb8d7f04d70, 5c6726b6-4e33-3535-86ae-d2b8563f1862, 6b85a9e1-6b06-34c3-94d1-e30b72de56ee, 1650f0c8-1146-35a8-993c-5b5c18ccf85e, bc346324-d6ff-39ed-b165-b878bb7afc21, 9fadbcbb-3c1c-3242-abab-d38c82081a5f, 4f1d08fd-8d8e-3227-8d8a-1f7f200fd058, 5b453d9e-f5ed-361e-bccb-fb36da1a5a0c)
```

Any one of these `subIds` can find its `parentId` if it exists else `None`.

```scala
val id = generator.sample()
// c4ca4238-a0b9-3382-8dcc-509a6f75849b

val subIds = generator.subIdsFromId(id)
// List(...)

val originalId = generator.idFromSubId(subIds.head)
// Some(c4ca4238-a0b9-3382-8dcc-509a6f75849b)
```
