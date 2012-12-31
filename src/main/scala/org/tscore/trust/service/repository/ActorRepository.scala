package org.tscore.trust.service.repository

import org.springframework.data.neo4j.repository.GraphRepository
import org.tscore.trust.model.Actor

trait ActorRepository extends GraphRepository[Actor] {
  def findByName(name: String): Actor
}
