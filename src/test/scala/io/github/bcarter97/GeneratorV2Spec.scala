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
  }
}
