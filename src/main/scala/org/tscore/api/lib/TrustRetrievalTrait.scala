package org.tscore.api.lib

import org.tscore.api.model.Subject
import org.tscore.api.model.Endorsement

import net.liftweb._
import common._

trait SubjectRetrival {
  def getAllSubjects: List[Subject]
  def findSubjectById(subjectId: String): Box[Subject]
  def searchSubjectsByKeyword(keyword: String): List[Subject]
  def addSubject(subject: Subject): Subject
  def deleteSubject(subjectId: String): Box[Subject]
  def prependSubjectListener(f: Subject => Unit)
}

trait EndorsementRetrival {
  def getAllEndorsements: List[Endorsement]
  def findEndorsementById(endorsementId: String): Box[Endorsement]
  def searchEndorsementsByKeyword(keyword: String): List[Endorsement]
  def addEndorsement(endorsement: Endorsement): Endorsement
  def deleteEndorsement(endorsementId: String): Box[Endorsement]
  def prependEndorsementListener(f: Endorsement => Unit)
}
