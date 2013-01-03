package org.tscore.web.lib.service.impl

import org.tscore.trust.service.EndorsementService
import org.tscore.trust.model.{Subject, Actor, Endorsement}
import org.tscore.trust.model.score.EndorsementScore
import org.tscore.web.lib.service.ServiceMock

class EndorsementServiceMock extends ServiceMock[Endorsement] with EndorsementService {
  def create(id: Option[java.lang.Long],
            actor: Actor,
            subject: Subject,
            score: Option[EndorsementScore]) = Option(
    Endorsement(
      id.getOrElse(null),
      actor,
      subject,
      score.getOrElse(null.asInstanceOf[EndorsementScore])
    )
  )
}
