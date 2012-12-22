package org.tscore.api.snippet

import org.tscore.api.lib.TrustRetrievalTrait
import org.tscore.api.model.{Subject, Endorsement}

import net.liftweb._
import util._
import Helpers._

import common.{Full, Empty, Box}
import net.liftweb.json._
import net.liftweb.util.Schedule

/**
 * Created with IntelliJ IDEA.
 * User: zahid
 * Date: 12/21/12
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
class TrustRetrievalMock extends TrustRetrievalTrait {
  private implicit val formats = net.liftweb.json.DefaultFormats + BigDecimalSerializer


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

  private var subjects = parse(subjectData).extract[List[Subject]]
  private var subjectListeners: List[Subject => Unit] = Nil

  //Subjects

  def getAllSubjects(): List[Subject] = {
    subjects
  }

  def findSubjectById(subjectId: String): Box[Subject] = synchronized {
    subjects.find(_.id == subjectId)
  }

  def searchSubjectsByKeyword(keyword: String): List[Subject] = {
    val keywordLC = keyword.toLowerCase()
    subjects.filter(i => i.name.toLowerCase.indexOf(keywordLC) >= 0 || i.description.toLowerCase.indexOf(keywordLC) >= 0)
  }

  def addSubject(subject: Subject): Subject = synchronized {
    subjects = subject :: subjects.filterNot(_.id == subject.id)
    updateSubjectListeners(subject)
  }

  def deleteSubject(subjectId: String): Box[Subject] = synchronized {
    var ret: Box[Subject] = Empty

    val Id = subjectId   //an upper case stable ID for pattern matching

    subjects = subjects.filter {
      case i@Subject(Id, _, _, _) =>
        ret = Full(i) // side effect
        false
      case _ => true
    }

    ret.map(updateSubjectListeners)
  }

  def prependSubjectListener(f: Subject => Unit) = {
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

  //Endorsements

  def getAllEndorsements(): List[Endorsement] = {
    endorsements
  }

  def findEndorsementById(endorsementId: String): Box[Endorsement] = synchronized {
    endorsements.find(_.id == endorsementId)
  }

  def searchEndorsementsByKeyword(keyword: String): List[Endorsement] = {
    val keywordLC = keyword.toLowerCase()
    endorsements.filter(i => i.description.toLowerCase.indexOf(keywordLC) >= 0)
  }

  def addEndorsement(endorsement: Endorsement): Endorsement = synchronized {
    endorsements = endorsement :: endorsements.filterNot(_.id == endorsement.id)
    updateEndorsementListeners(endorsement)
  }

  def deleteEndorsement(endorsementId: String): Box[Endorsement] = synchronized {
    var ret: Box[Endorsement] = Empty

    val Id = endorsementId   //an upper case stable ID for pattern matching

    endorsements = endorsements.filter {
      case i@Endorsement(Id, _, _) =>
        ret = Full(i) // side effect
        false
      case _ => true
    }

    ret.map(updateEndorsementListeners)
  }

  def prependEndorsementListener(f: Endorsement => Unit) = {
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
}
