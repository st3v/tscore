package org.tscore.trust.model

import score.ActorScore
import org.springframework.data.neo4j.annotation.Indexed
import org.springframework.data.neo4j.support.index.IndexType

class Actor extends org.tscore.graph.model.Actor  {

  @Indexed(indexName = "name", indexType = IndexType.FULLTEXT)
  var name : String = null

  var description: String = null

  var score: ActorScore = null

  override def equals(obj:Any) : Boolean = {
    super.equals(obj) &&
      obj.asInstanceOf[Actor].name == this.name &&
      obj.asInstanceOf[Actor].description == this.description &&
      obj.asInstanceOf[Actor].score == this.score
  }
}

object Actor {
  def apply(id: java.lang.Long = null,
            name: String,
            description: String = null,
            score: ActorScore = null) = {
    var actor = null.asInstanceOf[Actor]
    if (Option(name).isDefined) {
      actor = new Actor()
      actor.id = Option(id).getOrElse(null)
      actor.name = name
      actor.description = Option(description).getOrElse("")
      actor.score = Option(score).getOrElse(ActorScore.zero)
    }
    actor
  }
}