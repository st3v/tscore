package org.tscore.trust.model

class Endorsement extends org.tscore.graph.model.Endorsement {

}

object Endorsement {
  def apply(actor: org.tscore.trust.model.Actor,
            subject: org.tscore.trust.model.Subject,
            score: org.tscore.trust.model.score.EndorsementScore) = {
    val endorsement = new Endorsement()
    endorsement.actor = actor
    endorsement.subject = subject
    endorsement.score = score
    endorsement
  }
}
