package org.tscore.trust.service

import org.tscore.trust.model.Subject

trait SubjectServiceTrait {
  def createSubject(id: Option[Long], name: String, description: Option[String]): Option[Subject]
  def getAllSubjects: Seq[Subject]
  def findSubjectById(subjectId: Long): Option[Subject]
  def searchSubjectsByKeyword(keyword: String): Seq[Subject]
  def addSubject(subject: Subject): Option[Subject]
  def deleteSubject(subjectId: Long): Option[Subject]
  def prependSubjectListener(f: Subject => Unit)
}
