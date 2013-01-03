package org.tscore.trust.model

import score.ActorScore
import org.springframework.data.neo4j.annotation.Indexed
import org.springframework.data.neo4j.support.index.IndexType

class Actor extends org.tscore.graph.model.Actor  {

  @Indexed(indexName = "name", indexType = IndexType.FULLTEXT)
  var name : String = null

  var description: String = null

  var score: ActorScore = null
}

object Actor {
  def apply(id: java.lang.Long = null,
            name: String,
            description: String = null,
            score: ActorScore = null) = {
    val actor = new Actor()
    if (id != null) actor.id = id
    actor.name = name
    actor.description = Option(description).getOrElse("")
    actor.score = Option(score).getOrElse(ActorScore.zero)
    actor
  }
}