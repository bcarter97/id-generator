package io.github.bcarter97

import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

import cats.implicits._

import scala.collection.concurrent.TrieMap

case class Generator(maxIndex: Int = 1000000, maxSubIds: Int = 10) {

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

  def primaryId(): PrimaryId = sample()

  def primaryIds(startIndex: Int, endIndex: Int): Seq[PrimaryId] =
    (math.max(startIndex, 1) to math.min(endIndex, maxIndex)).map(primaryId)

  def index(uuid: UUID): Option[Int] = uuidToPrimaryId.get(uuid) match {
    case Some(id) => id.index.some
    case None     =>
      (1 to maxIndex).collectFirst {
        case i if primaryId(i).uuid == uuid => i
      }
  }

  def subIdFromUuid(uuid: UUID): Option[SubId] = uuidToSubId.get(uuid) orElse (1 to maxIndex).collectFirst { i =>
    primaryId(i).subIds.find(_.uuid == uuid) match {
      case Some(id) => id
    }
  }

  def sample(n: Int): Seq[PrimaryId] =
    if (n < 1) throw new IllegalArgumentException("n must be greater than 0")
    else
      (sampleCounter.get() until sampleCounter.getAndIncrement() + n).map(index => primaryId(index % maxIndex))

  def sample(): PrimaryId = sample(1).headOption.getOrElse(primaryId(1))

}
