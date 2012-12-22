package org.tscore.api.lib

import org.tscore.api.model.Subject
import org.tscore.api.model.Endorsement

import net.liftweb._
import common._

/**
 * Created with IntelliJ IDEA.
 * User: zahid
 * Date: 12/21/12
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
trait TrustRetrievalTrait {
  //Subjects
  def getAllSubjects(): List[Subject]
  def findSubjectById(subjectId: String): Box[Subject]
  def searchSubjectsByKeyword(keyword: String): List[Subject]
  def addSubject(subject: Subject): Subject
  def deleteSubject(subjectId: String): Box[Subject]
  def prependSubjectListener(f: Subject => Unit)

  /**
  //Endorsements
  def findEndorsementById(endorsementId: String): Subject
  def searchEndorsementsByKeyword(keyword: String): List[Subject]
  def addEndorsement(endorsement: Endorsement): Subject
  def deleteEndorsement(endorsementId: String): Box[Subject]
    */
}
