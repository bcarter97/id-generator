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

generator.primaryId(5)
// PrimaryId(index=5, uuid=e4da3b7f-bbce-3345-9777-2b0674a318d5, numSubIds=1)
```

### Generating PrimaryIds

As well as using `generator.id(n)` as above, there are a number of ways to generate a PrimaryId.

To sample a random PrimaryId (starting from the first index), use `sample`. This will automatically increment the sample index until the specified `maxIndex` is reached, at which point it will loop back around.

```scala
val generator = Generator(2)

val id1 = generator.sample()
// PrimaryId(index=1, uuid=c4ca4238-a0b9-3382-8dcc-509a6f75849b, numSubIds=9)

val id2 = generator.sample()
// PrimaryId(index=2, uuid=c81e728d-9d4c-3f63-af06-7f89cc14862c, numSubIds=9)

val id3 = generator.sample()
// PrimaryId(index=1, uuid=c4ca4238-a0b9-3382-8dcc-509a6f75849b, numSubIds=9) <-- same as id1
```

> :bulb: Using `id()` without an index will call the underlying `sample` method.

You can sample a range of PrimaryIds by passing a number to `sample`:

```scala
val ids = generator.sample(2)
// Vector(PrimaryId(index=0, uuid=cfcd2084-95d5-35ef-a6e7-dff9f98764da, numSubIds=9), PrimaryId(index=1, uuid=c4ca4238-a0b9-3382-8dcc-509a6f75849b, numSubIds=9))
```

Similarly, you can specify the range of UUIDs to generate, to make the list reproducible:

```scala
val ids = generator.ids(5, 8)
// Vector(PrimaryId(index=5, uuid=e4da3b7f-bbce-3345-9777-2b0674a318d5, numSubIds=1), PrimaryId(index=6, uuid=1679091c-5a88-3faf-afb5-e6087eb1b2dc, numSubIds=9), PrimaryId(index=7, uuid=8f14e45f-ceea-367a-9a36-dedd4bea2543, numSubIds=6))
```

### Generating SubIds

SubIds are derived from a PrimaryId. Each PrimaryId has a random, reproducible number of SubIds. Every generated SubId contains the PrimaryId that generated it.

To get a list of subIds from a parentId:

```scala
val id = generator.sample()

val subIds = id.subIds
// Vector(SubId(index=1, uuid=6512bd43-d9ca-36e0-ac99-0b0a82652dca, primaryId=PrimaryId(index=1, uuid=c4ca4238-a0b9-3382-8dcc-509a6f75849b, numSubIds=9)) ... )
```

Any one of these `subIds` can find its `parentId`.

```scala
val id = generator.sample()
// PrimaryId(index=1, uuid=c4ca4238-a0b9-3382-8dcc-509a6f75849b, numSubIds=9)

val subId = id.subIds.head
// SubId(index=1, uuid=6512bd43-d9ca-36e0-ac99-0b0a82652dca, primaryId=PrimaryId(index=1, uuid=c4ca4238-a0b9-3382-8dcc-509a6f75849b, numSubIds=9))

val originalId = generator.subIdFromUuid(subId.uuid)
// Some(SubId(index=1, uuid=6512bd43-d9ca-36e0-ac99-0b0a82652dca, primaryId=PrimaryId(index=1, uuid=c4ca4238-a0b9-3382-8dcc-509a6f75849b, numSubIds=9)))
```
