package io.github.bcarter97

import cats.implicits._

import java.util.UUID
import scala.collection.concurrent.TrieMap

case class GeneratorV2(maxIndex: Int = 1000000, maxSubIds: Int = 10) {

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
        newId
    }

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
      case Some(id) =>
        uuidToSubId.put(uuid, id)
        id
    }

  }

}
