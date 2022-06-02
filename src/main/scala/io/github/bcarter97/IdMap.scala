package io.github.bcarter97

import scala.collection.concurrent.TrieMap

final class IdMap[K, V]() {
  private val idMap: TrieMap[K, V] = TrieMap()

  def get(key: K): Option[V]           = idMap.get(key)
  def put(key: K, value: V): Option[V] = idMap.put(key, value)
  def clear(): Unit                    = idMap.clear()
}
