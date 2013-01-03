package org.tscore.trust.service.impl

import org.tscore.trust.service.EndorsementService
import org.tscore.trust.model.{Subject, Actor, Endorsement}
import org.springframework.beans.factory.annotation.Autowired
import org.tscore.trust.service.repository.EndorsementRepository
import org.tscore.trust.model.score.EndorsementScore

class EndorsementServiceImpl extends EndorsementService with CrudServiceImpl[Endorsement] with SearchServiceImpl[Endorsement] {
  @Autowired
  override protected val repository: EndorsementRepository = null

  def create(id: Option[java.lang.Long],
             actor: Actor,
             subject: Subject,
             score: Option[EndorsementScore]): Option[Endorsement] =
    Option(Endorsement(id.getOrElse(null), actor, subject, score.getOrElse(null)))

}
