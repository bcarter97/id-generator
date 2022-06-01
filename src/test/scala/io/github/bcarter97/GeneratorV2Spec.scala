package io.github.bcarter97

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class GeneratorV2Spec extends AnyWordSpecLike with Matchers {

  "GeneratorV2" should {
    "Generate a reproducible PrimaryId" in {
      val generatorV2 = GeneratorV2(maxIndex = 100)
      val id          = generatorV2.primaryId(50)
      val id2         = generatorV2.primaryId(50)

      id shouldBe id2
    }

    "Generate a range of reproducible PrimaryIds" in {
      val generator = GeneratorV2(maxIndex = 10)
      val ids       = generator.primaryIds(5, 10)
      val ids2      = generator.primaryIds(5, 10)

      ids shouldBe ids2
    }

    "guard against going out of bounds when generating a range of reproducible UUIDs" in {
      val generator = GeneratorV2(maxIndex = 10)
      val ids       = generator.primaryIds(-1, 11)
      val ids2      = generator.primaryIds(-1, 11)

      ids.length shouldBe 10
      ids2 shouldBe ids
    }

    "generate a reproducible amount of subIds given an id" in {
      val generator = GeneratorV2(maxIndex = 100)
      val id        = generator.primaryId(50)
      val id2       = generator.primaryId(50)

      id.numSubIds shouldBe id2.numSubIds
    }

    "generate a reproducible list of subIds given an id" in {
      val generator = GeneratorV2(maxIndex = 100)
      val id        = generator.primaryId(50)
      val id2       = generator.primaryId(50)

      id.subIds shouldBe id2.subIds
    }

    "return which index created a specific primaryId" in {
      val generator = GeneratorV2(maxIndex = 100)
      val id        = generator.primaryId(50)
      generator.clearCache()

      generator.index(id.uuid) shouldBe Some(50)
    }

    "return none if no index for a specific id was found" in {
      val generator = GeneratorV2(maxIndex = 100)
      val id        = generator.primaryId(500)
      generator.clearCache()

      generator.index(id.uuid) shouldBe None
    }

    "find the id that generated a list of subIds" in {
      val generator = GeneratorV2(maxIndex = 100)
      val id        = generator.primaryId(50)
      val subIds    = id.subIds
      generator.clearCache()

      subIds.foreach(subId => generator.subIdFromUuid(subId.uuid).map(_.primaryId) shouldBe Some(id))
    }

    "sample a range of ids" in {
      val generator = GeneratorV2(maxIndex = 100)
      val ids       = generator.sample(10)

      ids.length shouldBe 10
      ids.distinct.length shouldBe 10
    }

    "sample a single id if no parameter is specified" in {
      val generator = GeneratorV2(maxIndex = 100)
      val id        = generator.sample()
      val id2       = generator.sample()

      id should not be id2
    }

    "throw an error if sample is called with a number < 1" in {
      assertThrows[IllegalArgumentException] {
        GeneratorV2(1).sample(0)
      }
    }

    "call sample if no index is passed to id" in {
      val generator  = GeneratorV2(maxIndex = 100)
      val generator2 = GeneratorV2(maxIndex = 100)

      val id      = generator.primaryId()
      val nextId  = generator.primaryId()
      val id2     = generator2.primaryId()
      val nextId2 = generator2.primaryId()

      id should not be nextId
      id shouldBe id2
      nextId shouldBe nextId2
    }

    "return two distinct ranges of ids" in {
      val generator = GeneratorV2(maxIndex = 100)
      val ids       = generator.sample(10)
      val ids2      = generator.sample(10)

      ids.length shouldBe 10
      ids2.length shouldBe 10
      ids shouldNot contain theSameElementsAs ids2
    }

    "loop round to the original ids if the max index is hit" in {
      val generator = GeneratorV2(maxIndex = 10)
      val ids       = generator.sample(10)
      val ids2      = generator.sample(10)

      ids.length shouldBe 10
      ids2.length shouldBe 10
      ids should contain theSameElementsAs ids2
    }
  }
}
