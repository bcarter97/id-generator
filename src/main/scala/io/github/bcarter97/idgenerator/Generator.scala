import java.util.UUID

case class Generator(iterations: Int) {

  val lookupMap: Map[Int, String] =
    (1 to iterations).map(i => (i, UUID.nameUUIDFromBytes(i.toString.getBytes).toString)).toMap

  def stringToId(s: String) = UUID.nameUUIDFromBytes(s.getBytes).toString

}

object Generator {
  def apply() = new Generator(1000)
}
