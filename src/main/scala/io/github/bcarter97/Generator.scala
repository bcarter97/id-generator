package io.github.bcarter97

import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

import cats.implicits._

import scala.collection.concurrent.TrieMap

/** Instantiates a new ID generator.
  * @param maxIndex
  *   The maximum number of ids the generator can create.
  * @param maxSubIds
  *   The maximum number of subIds the generator can create for any given primary id.
  * @throws java.lang.IllegalArgumentException
  *   if maxIndex is less than 1 or subIds is less than 1.
  */
case class Generator(maxIndex: Int = 1000000, maxSubIds: Int = 10) {
  require(maxIndex > 0, "maxIndex must be greater than 0")
  require(maxSubIds > 0, "subIds must be greater than 0")

  private lazy val sampleCounter = new AtomicInteger(1)

  private val uuidToPrimaryId: TrieMap[UUID, PrimaryId] = new TrieMap[UUID, PrimaryId]().empty
  private val indexToPrimaryId: TrieMap[Int, PrimaryId] = new TrieMap[Int, PrimaryId]().empty
  private val uuidToSubId: TrieMap[UUID, SubId]         = new TrieMap[UUID, SubId]().empty

  def clearUuidToPrimaryIdCache(): Unit = uuidToPrimaryId.clear()
  def clearIndexCache(): Unit           = indexToPrimaryId.clear()
  def clearUuidToSubIdCache(): Unit     = uuidToSubId.clear()
  def clearCache(): Unit                = {
    clearUuidToPrimaryIdCache()
    clearIndexCache()
    clearUuidToSubIdCache()
  }

  /** Generates a new [[PrimaryId]] at the given index.
    * @param index
    *   The index to generate the UUID from.
    * @return
    *   a new [[PrimaryId]]
    */
  def primaryId(index: Int): PrimaryId =
    indexToPrimaryId.get(index) match {
      case Some(id) => id
      case None     =>
        val newId = PrimaryId(index, maxSubIds)
        indexToPrimaryId.put(index, newId)
        uuidToPrimaryId.put(newId.uuid, newId)
        newId.subIds.foreach(subId => uuidToSubId.put(subId.uuid, subId))
        newId
    }

  /** Generates a [[PrimaryId]] using [[sample]], making subsequent calls return unique IDs until maxIndex is reached.
    * @return
    *   a single, unique [[PrimaryId]].
    */
  def primaryId(): PrimaryId = sample()

  /** Generates a range of unique [[PrimaryId]]s.
    * @param startIndex
    *   The start index to generate the [[PrimaryId]]s from. Minimum index is 1.
    * @param endIndex
    *   The end index to generate the [[PrimaryId]]s from. Maximum index is [[maxIndex]].
    * @return
    *   a sequence of unique [[PrimaryId]].
    */
  def primaryIds(startIndex: Int, endIndex: Int): Seq[PrimaryId] =
    (math.max(startIndex, 1) to math.min(endIndex, maxIndex)).map(primaryId)

  /** Find the optional index of a [[PrimaryId]], or None if it was generated outside of the [[maxIndex]].
    * @param uuid
    *   The [[UUID]] to reverse.
    * @return
    *   the index of the UUID, or None if it is not a UUID generated within the [[maxIndex]].
    */
  def index(uuid: UUID): Option[Int] = uuidToPrimaryId.get(uuid) match {
    case Some(id) => id.index.some
    case None     =>
      (1 to maxIndex).collectFirst {
        case i if primaryId(i).uuid == uuid => i
      }
  }

  private[Generator] def findSubId(uuid: UUID, primaryId: PrimaryId): Option[SubId] =
    primaryId.subIds.find(_.uuid == uuid)

  /** @param uuid
    *   The [[UUID]] to find the original [[SubId]] for, else None if it was generated outside of the [[maxIndex]].
    * @return
    *   the [[SubId]] of the uuid if it exists.
    */
  def subIdFromUuid(uuid: UUID): Option[SubId] =
    uuidToSubId.get(uuid) orElse (1 to maxIndex).collectFirst { i =>
      findSubId(uuid, primaryId(i)) match {
        case Some(id) => id
      }
    }

  /** Samples a range of [[PrimaryId]]s.
    * @param n
    *   The number of PrimaryIds to sample.
    * @return
    *   `n` unique [[PrimaryId]]s.
    */
  def sample(n: Int): Seq[PrimaryId] =
    if (n < 1) throw new IllegalArgumentException("n must be greater than 0")
    else
      (sampleCounter.get() until sampleCounter.getAndIncrement() + n).map(index => primaryId(index % maxIndex))

  /** Generates a new [[PrimaryId]]
    * @return
    *   a single unique [[PrimaryId]].
    */
  def sample(): PrimaryId = sample(1).headOption.getOrElse(primaryId(1))

}
