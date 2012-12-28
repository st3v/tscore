package org.tscore.trust.service

import org.tscore.trust.model.Subject

trait SubjectServiceTrait {
  def getAllSubjects: Seq[Subject]
  def findSubjectById(subjectId: Long): Subject
  def searchSubjectsByKeyword(keyword: String): Seq[Subject]
  def addSubject(subject: Subject): Subject
  def deleteSubject(subjectId: Long)
  def prependSubjectListener(f: Subject => Unit)
}
