package org.tscore.graph.util

import org.neo4j.helpers.collection.ClosableIterable

object Conversions {
  implicit def toIterator[T](ci: ClosableIterable[T]) = new ScalaCloseableIterable[T](ci)
}
