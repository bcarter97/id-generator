package io.github.bcarter97

import java.util.UUID

case class Generator(maxIndex: Int = 1000000, subIds: Int = 10) {
  private var sampleCounter = 1;

  /** @param index
    *   The index to generate the UUID from.
    * @param parentId
    *   Optional parent UUID to generate the subId from.
    * @return
    *   Returns a reproducible UUID, which can be reversed to get either the original index, or if it is a subId, the
    *   original parentId.
    */
  def id(index: Int, parentId: Option[String] = None) =
    UUID.nameUUIDFromBytes(s"${index.toString}${parentId.getOrElse("")}".getBytes).toString

  private val idMap = (1 to maxIndex).map(index => (id(index), index)).toMap

  /** @param id
    *   The UUID to reverse.
    *
    * @return
    *   The index of the UUID, or None if it is not a valid UUID.
    */
  def index(id: String): Option[Int] = idMap.get(id)

  /** @param id
    *   the Id to find the number of subIds for.
    *
    * @return
    *   The number of SubIds for the given Id.
    */
  def numSubIdsForId(id: String): Int =
    (BigInt(id.getBytes.slice(0, 1)) % subIds).toInt

  /** @param id
    *   The Id to find the subIds for.
    *
    * @return
    *   The subIds for the given Id.
    */
  def subIdsFromId(parentId: String): Seq[String] =
    (1 to numSubIdsForId(parentId)).map(i => id(i, Some(parentId))).toList

  /** @param subId
    *   The subId to test.
    *
    * @param id
    *   The parent id to test the subId against.
    *
    * @return
    *   True if a subId was generated from the given parentId.
    */
  def isSubIdFromId(subId: String, parentId: String): Boolean =
    (1 to subIds).collectFirst {
      case i if id(i, Some(parentId)) == subId => i
    }.isDefined

  /** @param id
    *   The subId to find the original parentId index from.
    *
    * @return
    *   The index of the parentId if it exists.
    */
  def indexFromSubId(subId: String): Option[Int] =
    (1 to maxIndex).collectFirst { case i if isSubIdFromId(subId, id(i)) => i }

  /** @param id
    *   The subId to find the original parentId from.
    *
    * @return
    *   The parentId a subId is derived from it exists.
    */
  def idFromSubId(subId: String): Option[String] = indexFromSubId(subId).map(id(_))

  /** @param n
    *   The number of Ids to sample.
    *
    * @return
    *   Returns `n` unique UUIDS.
    */
  def sample(n: Int = 1): Seq[String] = {
    val ids = (sampleCounter to sampleCounter + n - 1).map(index => id(index % maxIndex))
    sampleCounter += n
    ids
  }

}
