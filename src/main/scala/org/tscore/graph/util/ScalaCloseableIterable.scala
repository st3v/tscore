package org.tscore.graph.util

import org.neo4j.helpers.collection.ClosableIterable

class ScalaCloseableIterable[T](ci: ClosableIterable[T]) {
  def foreach[U](f: (T) => U) {
    val i = ci.iterator()
    try {
      while (i.hasNext) {
        val t = i.next()
        f(t)
      }
    } finally {
      ci.close()
    }
  }
}