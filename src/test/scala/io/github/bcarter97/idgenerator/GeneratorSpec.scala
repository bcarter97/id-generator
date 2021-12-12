package io.github.bcarter97

import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class GeneratorSpec extends AnyWordSpecLike with Matchers with ScalaFutures with OptionValues {

  "Generator" should {
    "generate a reproducable UUID" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.id(50)
      val id2       = generator.id(50)

      id2 shouldBe id
    }

    "generate a reproducable amount of subIds given an id" in {
      val generator  = Generator(maxIndex = 100)
      val id         = generator.id(50)
      val id2        = generator.id(50)
      val numSubIds  = generator.numSubIdsForId(id)
      val numSubIds2 = generator.numSubIdsForId(id)

      numSubIds2 shouldBe numSubIds
    }

    "generate a reproducable list of subIds given an id" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.id(50)
      val id2       = generator.id(50)
      val subIds    = generator.generateSubIds(id)
      val subIds2   = generator.generateSubIds(id2)

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
      val subIds    = generator.generateSubIds(id)

      subIds.foreach(subId => generator.isSubIdFromId(id, subId) shouldBe true)
    }

    "find the id that generated a list of subIds" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.id(50)
      val subIds    = generator.generateSubIds(id)

      subIds.foreach(subId => generator.idFromSubId(subId) shouldBe Some(id))
    }

    "find the index that generated a list of subIds" in {
      val generator = Generator(maxIndex = 100)
      val index     = 50
      val id        = generator.id(index)
      val subIds    = generator.generateSubIds(id)

      subIds.foreach(subId => generator.indexFromSubId(subId) shouldBe Some(50))
    }

    "sample a range of ids" in {
      val generator = Generator(maxIndex = 100)
      val ids       = generator.sample(10)

      ids.length shouldBe 10
      ids.distinct.lengthCompare(ids) shouldBe 0
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
