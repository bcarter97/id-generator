import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class GeneratorSpec extends AnyWordSpecLike with Matchers with ScalaFutures with OptionValues {

  "Generator" should {
    "generate a reproducable UUID" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.indexToId(50)
      val id2       = generator.indexToId(50)

      id2 shouldBe id
    }

    "generate a reproducable amount of subIds given an id" in {
      val generator  = Generator(maxIndex = 100)
      val id         = generator.indexToId(50)
      val id2        = generator.indexToId(50)
      val numSubIds  = generator.numSubIdsForId(id)
      val numSubIds2 = generator.numSubIdsForId(id)

      numSubIds2 shouldBe numSubIds
    }

    "generate a reproducable list of subIds given an id" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.indexToId(50)
      val id2       = generator.indexToId(50)
      val subIds    = generator.generateSubIds(id)
      val subIds2   = generator.generateSubIds(id2)

      subIds shouldBe subIds2
    }

    "return which index created a specific id" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.indexToId(50)
      val idIndex   = generator.idToIndex(id)

      idIndex shouldBe Some(50)
    }

    "return none if no index for a specific id was found" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.indexToId(500)
      val idIndex   = generator.idToIndex(id)

      idIndex shouldBe None
    }

    "find if a subId is part of an id's sub group" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.indexToId(50)
      val subIds    = generator.generateSubIds(id)

      subIds.foreach(subId => generator.isSubIdFromId(id, subId) shouldBe true)
    }

    "find the id that generated a list of subIds" in {
      val generator = Generator(maxIndex = 100)
      val id        = generator.indexToId(50)
      val subIds    = generator.generateSubIds(id)

      subIds.foreach(subId => generator.idFromSubId(subId) shouldBe Some(id))
    }

    "find the index that generated a list of subIds" in {
      val generator = Generator(maxIndex = 100)
      val index     = 50
      val id        = generator.indexToId(index)
      val subIds    = generator.generateSubIds(id)

      subIds.foreach(subId => generator.indexFromSubId(subId) shouldBe Some(50))
    }
  }
}
