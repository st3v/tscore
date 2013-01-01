package org.tscore.web.lib.service.impl

import net.liftweb._
import util._
import Helpers._
import org.tscore.trust.service.EndorsementServiceTrait
import org.tscore.trust.model.{Subject, Actor, Endorsement}
import org.tscore.trust.model.score.EndorsementScore
import net.liftweb.util.Schedule

class EndorsementServiceMock extends ServiceMock[Endorsement] with EndorsementServiceTrait {
  def createEndorsement(id: Option[Long],
                        actor: Actor,
                        subject: Subject,
                        score: Option[EndorsementScore]) = Option(
    Endorsement(
      id.getOrElse(null.asInstanceOf[Long]),
      actor,
      subject,
      score.getOrElse(null.asInstanceOf[EndorsementScore])
    )
  )

  def getAllEndorsements: Seq[Endorsement] = {
    repository
  }

  def findEndorsementById(endorsementId: Long): Option[Endorsement] = synchronized {
    repository.find(_.id == endorsementId)
  }

  def addEndorsement(endorsement: Endorsement): Option[Endorsement] = synchronized {
    var result: Endorsement = null
    if (findEndorsementById(endorsement.id).isEmpty) {
      repository ::= endorsement
      updateListeners(endorsement)
      result = endorsement
    }
    Option(result)
  }

  def deleteEndorsement(endorsementId: Long): Option[Endorsement] = synchronized {
    val endorsement: Option[Endorsement] = findEndorsementById(endorsementId)
    if (endorsement.isDefined) {
      repository = repository.diff(List(endorsement.get))
      updateListeners(endorsement.get)
    }
    endorsement
  }

  def prependEndorsementListener(f: Endorsement => Unit) {
    listeners ::= f
  }

  private def updateListeners(endorsement: Endorsement) = {
    synchronized {
      listeners.foreach(f =>
        Schedule.schedule(() => f(endorsement), 0 seconds))

      listeners = Nil
    }
    endorsement
  }
}
