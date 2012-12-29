package org.tscore.trust.model

import score.EndorsementScore

class Endorsement extends org.tscore.graph.model.Endorsement {

  var score: EndorsementScore = null

  override def toString = "%s (score: %s)".format(super.toString, score)

  override def equals(obj: Any) = {
    super.equals(obj) && obj.asInstanceOf[Endorsement].score == score
  }
}

object Endorsement {
  def apply(id: Long = 0,
            actor: Actor,
            subject: Subject,
            score: EndorsementScore = null) = {
    val endorsement = new Endorsement()
    if (id>0) subject.id = id
    endorsement.actor = actor
    endorsement.subject = subject
    endorsement.score = Option(score).getOrElse(EndorsementScore.default)
    endorsement
  }

}
