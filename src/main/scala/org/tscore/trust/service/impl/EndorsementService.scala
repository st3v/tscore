package org.tscore.trust.service.impl

import org.tscore.trust.service.EndorsementServiceTrait
import org.tscore.trust.model.{Subject, Actor, Endorsement}
import scala.collection.JavaConversions._
import org.springframework.beans.factory.annotation.Autowired
import org.tscore.trust.service.repository.EndorsementRepository
import org.tscore.trust.model.score.EndorsementScore
import org.springframework.transaction.annotation.Transactional

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

  @Transactional
  def addEndorsement(endorsement: Endorsement) = Option(repository.save(endorsement))

  @Transactional
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
