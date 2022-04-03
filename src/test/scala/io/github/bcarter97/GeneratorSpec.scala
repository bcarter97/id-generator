package io.github.bcarter97

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class GeneratorSpec extends AnyWordSpecLike with Matchers {

  "Generator" should {
    "throw an error if Generator is called with an invalid maxIndex" in {
      assertThrows[IllegalArgumentException] {
        Generator(0)
      }
    }

    "throw an error if Generator is called with an invalid maxIndex" in {
      assertThrows[IllegalArgumentException] {
        Generator(1, 0)
      }
    }

    "generate a reproducible UUID" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.id(50)
      val id2       = generator.id(50)

      id2 shouldBe id
    }

    "generate a range of reproducible UUIDs" in {
      val generator = Generator(maxIndex = 10)
      val ids       = generator.ids(5, 7)
      val ids2      = generator.ids(5, 7)

      ids.length shouldBe 3
      ids2 shouldBe ids
    }

    "guard against going out of bounds when generating a range of reproducible UUIDs" in {
      val generator = Generator(maxIndex = 10)
      val ids       = generator.ids(-1, 11)
      val ids2      = generator.ids(-1, 11)

      ids.length shouldBe 10
      ids2 shouldBe ids
    }

    "generate a reproducible amount of subIds given an id" in {
      val generator  = Generator(maxIndex = 100)
      val id         = generator.id(50)
      val id2        = generator.id(50)
      val numSubIds  = generator.numSubIdsForId(id)
      val numSubIds2 = generator.numSubIdsForId(id2)

      numSubIds2 shouldBe numSubIds
    }

    "generate a reproducible list of subIds given an id" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.id(50)
      val id2       = generator.id(50)
      val subIds    = generator.subIdsFromId(id)
      val subIds2   = generator.subIdsFromId(id2)

      subIds shouldBe subIds2
    }

    "return which index created a specific id" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.id(50)
      val idIndex   = generator.index(id)

      idIndex shouldBe Some(50)
    }

    "return none if no index for a specific id was found" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.id(500)
      val idIndex   = generator.index(id)

      idIndex shouldBe None
    }

    "find if a subId is part of an id's sub group" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.id(50)
      val subIds    = generator.subIdsFromId(id)

      subIds.foreach(subId => generator.isSubIdFromId(subId, id) shouldBe true)
    }

    "find the id that generated a list of subIds" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.id(50)
      val subIds    = generator.subIdsFromId(id)

      subIds.foreach(subId => generator.idFromSubId(subId) shouldBe Some(id))
    }

    "find the index that generated a list of subIds" in {
      val generator = Generator(maxIndex = 100)
      val index     = 50
      val id        = generator.id(index)
      val subIds    = generator.subIdsFromId(id)

      subIds.foreach(subId => generator.indexFromSubId(subId) shouldBe Some(50))
    }

    "sample a range of ids" in {
      val generator = Generator(maxIndex = 100)
      val ids       = generator.sample(10)

      ids.length shouldBe 10
      ids.distinct.length shouldBe 10
    }

    "sample a single id if no parameter is specified" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.sample()
      val id2       = generator.sample()

      id should not be id2
    }

    "throw an error if sample is called with a number < 1" in {
      assertThrows[IllegalArgumentException] {
        Generator(1).sample(0)
      }
    }

    "call sample if no index is passed to id" in {
      val generator  = Generator(maxIndex = 100)
      val generator2 = Generator(maxIndex = 100)

      val id      = generator.id()
      val nextId  = generator.id()
      val id2     = generator2.id()
      val nextId2 = generator2.id()

      id should not be nextId
      id shouldBe id2
      nextId shouldBe nextId2
    }

    "return two distinct ranges of ids" in {
      val generator = Generator(maxIndex = 100)
      val ids       = generator.sample(10)
      val ids2      = generator.sample(10)

      ids.length shouldBe 10
      ids2.length shouldBe 10
      ids shouldNot contain theSameElementsAs ids2
    }

    "loop round to the original ids if the max index is hit" in {
      val generator = Generator(maxIndex = 10)
      val ids       = generator.sample(10)
      val ids2      = generator.sample(10)

      ids.length shouldBe 10
      ids2.length shouldBe 10
      ids should contain theSameElementsAs ids2
    }
  }
}
