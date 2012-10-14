package org.tscore.graph.model

import org.springframework.data.neo4j.annotation._

@RelationshipEntity(`type` = "ENDORSES")
class Endorsement {
  @GraphId
  var id: java.lang.Long = _

  @StartNode
  var actor: Actor = _

  @EndNode
  var subject: Subject = _

  var score: Double = 0.0

  override def toString = "%s %s (score: %s) %s".format(actor, "ENDORSES", score, subject)

  override def equals(obj:Any) = obj.isInstanceOf[Endorsement] &&
                                 obj.asInstanceOf[Endorsement].id == this.id &&
                                 obj.asInstanceOf[Endorsement].actor.id == this.actor.id &&
                                 obj.asInstanceOf[Endorsement].subject.id == this.subject.id &&
                                 obj.asInstanceOf[Endorsement].score == this.score

}

object Endorsement {
  def apply(actor: Actor, subject: Subject, score: Double) = {
    val endorsement = new Endorsement()
    endorsement.actor = actor
    endorsement.subject = subject
    endorsement.score = score
    endorsement
  }
}
