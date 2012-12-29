package org.tscore.web.mock

import net.liftweb._
import util._
import Helpers._

import net.liftweb.json._
import net.liftweb.util.Schedule
import org.tscore.trust.service.{ActorServiceTrait, EndorsementServiceTrait, SubjectServiceTrait}
import org.tscore.trust.model.{Actor, Endorsement, Subject}
import org.tscore.trust.model.score.{ActorScore, EndorsementScore}

class TrustRetrievalMock extends ActorServiceTrait with SubjectServiceTrait with EndorsementServiceTrait {
  private implicit val formats = net.liftweb.json.DefaultFormats


  /*******************************************
  ********          SUBJECTS       ***********
  ********************************************/


  // The raw data
  private def subjectData =
    """[
  {
    "id": "1234",
    "name": "Tim the Baker",
    "description": "Needs a Kiva Zip loan to bake goodies",
    "loanQuantity": 200
  },
  {
    "id": "4567",
    "name": "Jeremy the social worker",
    "description": "Needs a Kiva Zip loan to help his community",
    "loanQuantity": 800
  },
  ]"""

  private var subjects: List[Subject] = parse(subjectData).extract[List[Subject]]
  private var subjectListeners: List[Subject => Unit] = Nil

  def createSubject(id: Option[Long], name: String, description: Option[String]): Option[Subject] = {
    Option(Subject(id.getOrElse(null.asInstanceOf[Long]), name, description.getOrElse(null)))
  }

  def getAllSubjects: Seq[Subject] = {
    subjects
  }

  def findSubjectById(subjectId: Long): Option[Subject] = synchronized {
    subjects.find(_.id == subjectId)
  }

  def searchSubjectsByKeyword(keyword: String): Seq[Subject] = {
    val keywordLC = keyword.toLowerCase
    subjects.filter(i => i.name.toLowerCase.indexOf(keywordLC) >= 0 || i.description.toLowerCase.indexOf(keywordLC) >= 0)
  }

  def addSubject(subject: Subject): Option[Subject] = synchronized {
    var result: Subject = null
    if (findSubjectById(subject.id).isEmpty) {
      subjects = subject :: subjects
      updateSubjectListeners(subject)
      result = subject
    }
    Option(result)
  }

  def deleteSubject(subjectId: Long): Option[Subject] = synchronized {
    val subject: Option[Subject] = findSubjectById(subjectId)
    if (subject.isDefined) {
      subjects = subjects.diff(List(subject.get))
      updateSubjectListeners(subject.get)
    }
    subject
  }

  def prependSubjectListener(f: Subject => Unit) {
    //prepend the function to the list of listeners
    subjectListeners ::= f
  }


  /*******************************************
  ********       ENDORSEMENTS      ***********
  ********************************************/


  // The raw data
  private def endorsementData =
    """[
  {
    "id": "1",
    "description": "Made a repayment",
    "size": 20
  },
  {
    "id": "2",
    "description": "Had a recommendation make a repayment",
    "size": 10
  },
  ]"""

  private var endorsements = parse(endorsementData).extract[List[Endorsement]]
  private var endorsementListeners: List[Endorsement => Unit] = Nil

  def createEndorsement(id: Option[Long],
                        actor: Actor,
                        subject: Subject,
                        score: Option[EndorsementScore]): Option[Endorsement] = {
    Option(Endorsement(id.getOrElse(null.asInstanceOf[Long]), actor, subject, score.getOrElse(null)))
  }

  def getAllEndorsements: Seq[Endorsement] = {
    endorsements
  }

  def findEndorsementById(endorsementId: Long): Option[Endorsement] = synchronized {
    endorsements.find(_.id == endorsementId)
  }

  def addEndorsement(endorsement: Endorsement): Option[Endorsement] = synchronized {
    var result: Endorsement = null
    if (findEndorsementById(endorsement.id).isEmpty) {
      endorsements = endorsement :: endorsements
      updateEndorsementListeners(endorsement)
      result = endorsement
    }
    Option(result)
  }

  def deleteEndorsement(endorsementId: Long): Option[Endorsement] = synchronized {
    val endorsement = findEndorsementById(endorsementId)
    if (endorsement.isDefined) {
      endorsements = endorsements.diff(List(endorsement.get))
      updateEndorsementListeners(endorsement.get)
    }
    endorsement
  }

  def prependEndorsementListener(f: Endorsement => Unit) {
    //prepend the function to the list of listeners
    endorsementListeners ::= f
  }

  //Update subject listeners when the data changes
  private def updateSubjectListeners(subject: Subject): Subject = {
    synchronized {
      subjectListeners.foreach(f =>
        Schedule.schedule(() => f(subject), 0 seconds))

      subjectListeners = Nil
    }
    subject
  }

  //Update endorsement listeners when the data changes
  private def updateEndorsementListeners(endorsement: Endorsement): Endorsement = {
    synchronized {
      endorsementListeners.foreach(f =>
        Schedule.schedule(() => f(endorsement), 0 seconds))

      endorsementListeners = Nil
    }
    endorsement
  }

  def createActor(id: Option[Long], name: String, description: Option[String], score: Option[ActorScore]): Option[Actor] = Option(null)

  def getAllActors: List[Actor] = List.empty[Actor]

  def findActorById(actorId: Long): Option[Actor] = Option(null)

  def findActorByName(actorName: String): Option[Actor] = Option(null)

  def searchActorsByKeyword(keyword: String): List[Actor] = List.empty[Actor]

  def addActor(actor: Actor): Option[Actor] = Option(null)

  def deleteActor(actorId: Long): Option[Actor] = Option(null)

  def prependActorListener(f: (Actor) => Unit) {}
}
