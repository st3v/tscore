package org.tscore.trust.service

import org.tscore.trust.model.{Subject, Actor, Endorsement}
import org.tscore.trust.model.score.EndorsementScore

trait EndorsementServiceTrait {
  def createEndorsement(id: Option[Long], actor: Actor, subject: Subject, score: Option[EndorsementScore]): Option[Endorsement]
  def getAllEndorsements: Seq[Endorsement]
  def findEndorsementById(endorsementId: Long): Option[Endorsement]
  def addEndorsement(endorsement: Endorsement): Option[Endorsement]
  def deleteEndorsement(endorsementId: Long): Option[Endorsement]
  def prependEndorsementListener(f: Endorsement => Unit)
}
