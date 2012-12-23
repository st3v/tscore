package org.tscore.graph.repository

import org.springframework.data.neo4j.repository.GraphRepository
import org.tscore.graph.model.Actor

trait ActorRepository extends GraphRepository[Actor] {
  def findActorById(id: java.lang.Long): Actor
  def findActorByName(name: String): Actor
}
