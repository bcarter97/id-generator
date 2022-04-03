package io.github.bcarter97

import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

case class Generator(maxIndex: Int = 1000000, subIds: Int = 10) {
  require(maxIndex > 0, "maxIndex must be greater than 0")

  lazy private val sampleCounter = new AtomicInteger(1)

  /** @param index
    *   The index to generate the UUID from.
    * @param parentId
    *   Optional parent UUID to generate the subId from.
    * @return
    *   Returns a reproducible UUID, which can be reversed to get either the original index, or if it is a subId, the
    *   original parentId.
    */
  def id(index: Int, parentId: Option[String] = None): String =
    UUID.nameUUIDFromBytes(s"${index.toString}${parentId.getOrElse("")}".getBytes).toString

  /** @param startIndex
    *   The start index to generate the UUIDs from. Minimum index is 1.
    * @param endIndex
    *   The end index to generate the UUIDs from. Maximum index is [[maxIndex]].
    * @return
    *   Returns a range of unique UUIDs.
    */
  def ids(startIndex: Int = 0, endIndex: Int = maxIndex): Seq[String] =
    (math.max(startIndex, 1) to math.min(endIndex, maxIndex)).map(index => id(index))

  /** Generates an id using [[sample]], making subsequent calls return unique IDs until [[maxIndex]] is reached.
    * @return
    *   Returns a single, unique ID.
    */
  def id(): String = sample()

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

  /** @param parentId
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
    * @param parentId
    *   The id to test the subId against.
    *
    * @return
    *   True if a subId was generated from the given parentId.
    */
  def isSubIdFromId(subId: String, parentId: String): Boolean =
    (1 to subIds).collectFirst {
      case i if id(i, Some(parentId)) == subId => i
    }.isDefined

  /** @param subId
    *   The subId to find the original parentId index from.
    *
    * @return
    *   The index of the parentId if it exists.
    */
  def indexFromSubId(subId: String): Option[Int] =
    (1 to maxIndex).collectFirst { case i if isSubIdFromId(subId, id(i)) => i }

  /** @param subId
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
    *   Returns `n` unique UUIDs.
    */
  def sample(n: Int): Seq[String] =
    if (n < 1) throw new IllegalArgumentException("n must be greater than 0")
    else (sampleCounter.get() until sampleCounter.getAndIncrement() + n).map(index => id(index % maxIndex))

  /** @return
    *   Returns a single unique UUID.
    */
  def sample(): String = sample(1).headOption.getOrElse(id(1))

}
