package org.tscore.trust.service.impl

import org.springframework.data.neo4j.repository.GraphRepository
import org.tscore.trust.service.SearchService

trait SearchServiceImpl[T] extends SearchService[T] {
  protected val repository: GraphRepository[T] = null

  def search(keyword: String): List[T] = List[T]()
}
