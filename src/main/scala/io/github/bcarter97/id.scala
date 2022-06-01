package io.github.bcarter97

import java.util.UUID

sealed trait Id {
  def index: Int
  def uuid: UUID
  def value: String
}

case class PrimaryId(index: Int, maxSubIds: Int) extends Id {
  val uuid: UUID = UUID.nameUUIDFromBytes(index.toString.getBytes)

  val value: String = uuid.toString

  val numSubIds: Int =
    (BigInt(this.value.getBytes.slice(0, 1)) % maxSubIds).toInt

  val subIds: Seq[SubId] = (1 to numSubIds).map(SubId(_, this))
}

case class SubId(index: Int, primaryId: PrimaryId) extends Id {
  val uuid: UUID = UUID.nameUUIDFromBytes(s"$index${primaryId.index}".getBytes)

  val value: String = uuid.toString
}
