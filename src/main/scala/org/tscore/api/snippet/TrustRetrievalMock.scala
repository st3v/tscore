package org.tscore.api.snippet

import org.tscore.api.lib.TrustRetrievalTrait
import org.tscore.api.model.{Endorsement, Subject}

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

  // The raw data
  private def data =
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

  private var subjects = parse(data).extract[List[Subject]]
  private var listeners: List[Subject => Unit] = Nil

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
    updateListeners(subject)
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

    ret.map(updateListeners)
  }

  def prependSubjectListener(f: Subject => Unit) = {
    //prepend the function to the list of listeners
    listeners ::= f
  }

  /**

  //Endorsements
  def findEndorsementById(endorsementId: String): Subject
  def searchEndorsementsByKeyword(keyword: String): List[Subject]
  def addEndorsement(endorsement: Endorsement): Subject
  def deleteEndorsement(endorsementId: String): Box[Subject]

  */

  //Update listeners when the data changes
  private def updateListeners(subject: Subject): Subject = {
    synchronized {
      listeners.foreach(f =>
        Schedule.schedule(() => f(subject), 0 seconds))

      listeners = Nil
    }
    subject
  }
}
