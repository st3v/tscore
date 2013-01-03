package org.tscore.trust.service

import org.tscore.trust.model.{Subject, Actor, Endorsement}
import org.tscore.trust.model.score.EndorsementScore

trait EndorsementService extends CrudService[Endorsement] {
  def create(id: Option[java.lang.Long], actor: Actor, subject: Subject, score: Option[EndorsementScore]): Option[Endorsement]
}
