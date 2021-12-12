package io.github.bcarter97

import java.util.UUID

case class Generator(maxIndex: Int = 1000000, subIds: Int = 10) {
  private var sampleCounter = 1;

  /** @param index
    *   The index to generate the UUID from.
    * @param parentId
    *   Optional parent UUID to generate the subId from.
    * @return
    *   Returns a reproducible UUID, which can be reversed to get the index, or if it is a subId, the original parentId.
    */
  def id(index: Int, parentId: Option[String] = None) =
    UUID.nameUUIDFromBytes(s"${index.toString}${parentId.getOrElse("")}".getBytes).toString

  private val idMap = (1 to maxIndex).map(index => (id(index), index)).toMap

  def index(id: String): Option[Int] = idMap.get(id)

  def numSubIdsForId(id: String): Int =
    (BigInt(id.getBytes.slice(0, 1)) % subIds).toInt

  def generateSubIds(parentId: String): Seq[String] = {
    val subIds = numSubIdsForId(parentId)
    (1 to subIds).map(i => id(i, Some(parentId))).toList
  }

  def isSubIdFromId(parentId: String, subId: String): Boolean =
    (1 to subIds).collectFirst {
      case i if id(i, Some(parentId)) == subId => i
    }.isDefined

  def indexFromSubId(subId: String): Option[Int] =
    (1 to maxIndex).collectFirst { case i if isSubIdFromId(id(i), subId) => i }

  def idFromSubId(subId: String): Option[String] = indexFromSubId(subId).map(id(_))

  def sample(n: Int = 1): Seq[String] = {
    val ids = (sampleCounter to sampleCounter + n - 1).map(index => id(index % maxIndex))
    sampleCounter += n
    ids
  }

}
