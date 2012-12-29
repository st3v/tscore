package org.tscore.web.lib.controller

import org.springframework.stereotype.Controller
import org.springframework.beans.factory.annotation.Autowired
import org.tscore.trust.service.{SubjectServiceTrait, ActorServiceTrait, EndorsementServiceTrait}
import net.liftweb.common.{Empty, Full, Box}
import org.tscore.trust.model.score.EndorsementScore
import org.tscore.web.model.Endorsement

class EndorsementController {}

@Controller
object EndorsementController {
  @Autowired
  private val endorsementService : EndorsementServiceTrait = null

  @Autowired
  private val actorService : ActorServiceTrait = null

  @Autowired
  private val subjectService : SubjectServiceTrait = null

  implicit def intToEndosementScore(in: Int): EndorsementScore = new EndorsementScore(in)

  implicit def intOptionToEndorsementScoreOption(in: Option[Int]): Option[EndorsementScore] = in match {
    case Some(s) => Some(intToEndosementScore(s))
    case None => null
  }

  implicit def toEndorsement(in: org.tscore.trust.model.Endorsement): Endorsement = {
    new Endorsement(Option(in.id), in.actor.id, in.subject.id, Option(in.score))
  }

  implicit def toEndorsementSeq(in: Seq[org.tscore.trust.model.Endorsement]): Seq[Endorsement] = {
    in.map(toEndorsement)
  }

  implicit def toEndorsementBox(in: Option[org.tscore.trust.model.Endorsement]): Box[Endorsement] = {
    in match {
      case Some(s) => Full(toEndorsement(s))
      case None => Empty }
  }

  implicit def fromEndorsement(in: Endorsement): org.tscore.trust.model.Endorsement = {
    var result: org.tscore.trust.model.Endorsement = null

    val subject = subjectService.findSubjectById(in.subjectId)
    val actor = actorService.findActorById(in.actorId)

    if (actor.isDefined && subject.isDefined) {
      result = endorsementService.createEndorsement(
        in.id,
        actor.get,
        subject.get,
        in.score).getOrElse(null)
    }

    result
  }

  //Get all endorsements
  def allEndorsements: Seq[Endorsement] = endorsementService.getAllEndorsements

  //Find a Endorsement by ID
  def find(id: Long): Box[Endorsement] = endorsementService.findEndorsementById(id)

  //Add a endorsement
  def add(endorsement: Endorsement): Box[Endorsement] = endorsementService.addEndorsement(endorsement)

  //Deletes the endorsement with id and returns the deleted endorsement or Empty if there's no match
  def delete(id: Long): Box[Endorsement] = endorsementService.deleteEndorsement(id)

  //Add an onChange listener
  def onChange(f: Endorsement => Unit) {
    synchronized {
      def g(endorsement: org.tscore.trust.model.Endorsement) {
        f(endorsement)
      }
      endorsementService.prependEndorsementListener(g)
    }
  }
}
