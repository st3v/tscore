package org.tscore.trust.service.impl

import org.tscore.trust.service.EndorsementServiceTrait
import org.tscore.trust.model.{Subject, Actor, Endorsement}
import scala.collection.JavaConversions._
import org.springframework.beans.factory.annotation.Autowired
import org.tscore.trust.repository.EndorsementRepository
import org.tscore.trust.model.score.EndorsementScore

class EndorsementService extends EndorsementServiceTrait {
  @Autowired
  private val repository: EndorsementRepository = null

  def createEndorsement(id: Option[Long],
                        actor: Actor,
                        subject: Subject,
                        score: Option[EndorsementScore]): Option[Endorsement] =
    Option(Endorsement(id.getOrElse(null.asInstanceOf[Long]), actor, subject, score.getOrElse(null)))

  def getAllEndorsements = repository.findAll().as(classOf[java.util.List[Endorsement]])

  def findEndorsementById(endorsementId: Long) = Option(repository.findOne(endorsementId))

  def addEndorsement(endorsement: Endorsement) = {
    if (endorsement.id != null && !repository.exists(endorsement.id)) {
      endorsement.id = null
    }
    Option(repository.save(endorsement))
  }

  def deleteEndorsement(endorsementId: Long): Option[Endorsement] = {
    findEndorsementById(endorsementId) match {
      case Some(endorsement) => {
        repository.delete(endorsementId)
        Some(endorsement)
      }
      case None => None
    }
  }

  def prependEndorsementListener(f: (Endorsement) => Unit) {}
}
