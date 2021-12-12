package io.github.bcarter97

import java.util.UUID

case class Generator(maxIndex: Int = 1000000, subIds: Int = 10) {

  def indexToId(index: Int, seed: Option[String] = None) =
    UUID.nameUUIDFromBytes(s"${index.toString}${seed.getOrElse("")}".getBytes).toString

  val idMap = (1 to maxIndex).map(index => (indexToId(index), index)).toMap

  def idToIndex(id: String): Option[Int] = idMap.get(id)

  def numSubIdsForId(id: String): Int =
    (BigInt(id.getBytes.slice(0, 1)) % subIds).toInt

  def generateSubIds(id: String): Seq[String] = {
    val subIds = numSubIdsForId(id)
    (1 to subIds).map(i => indexToId(i, Some(id))).toList
  }

  def isSubIdFromId(id: String, subId: String): Boolean =
    (1 to subIds).collectFirst {
      case i if indexToId(i, Some(id)) == subId => i
    }.isDefined

  def indexFromSubId(subId: String): Option[Int] =
    (1 to maxIndex).collectFirst { case i if isSubIdFromId(indexToId(i), subId) => i }

  def idFromSubId(subId: String): Option[String] = indexFromSubId(subId).map(indexToId(_))

}
