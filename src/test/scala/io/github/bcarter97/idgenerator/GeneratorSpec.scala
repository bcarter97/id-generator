import org.scalatest.OptionValues
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class GeneratorSpec extends AnyWordSpecLike with Matchers with ScalaFutures with OptionValues {

  "Generator" should {
    "generate a random string" in {
      val generator = Generator()
      val result    = generator.stringToId("foo")
      result shouldBe "acbd18db-4cc2-385c-adef-654fccc4a4d8"
    }
  }
}
