package org.tscore.graph.model

import org.springframework.data.neo4j.annotation._

@RelationshipEntity(`type` = "ENDORSES")
class Endorsement {
  @GraphId
  var id: java.lang.Long = null

  @StartNode
  var actor: Actor = null

  @EndNode
  var subject: Subject = null

  override def toString = "%s %s %s".format(actor, "ENDORSES", subject)

  override def equals(obj:Any) = obj.isInstanceOf[Endorsement] &&
                                 obj.asInstanceOf[Endorsement].id == this.id &&
                                 obj.asInstanceOf[Endorsement].actor.id == this.actor.id &&
                                 obj.asInstanceOf[Endorsement].subject.id == this.subject.id

}

object Endorsement {
  def apply(actor: Actor, subject: Subject) = {
    val endorsement = new Endorsement
    endorsement.actor = actor
    endorsement.subject = subject
    endorsement
  }
}

