package org.tscore.trust.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.tscore.trust.repository.SubjectRepository
import org.tscore.trust.model.Subject
import org.tscore.trust.service.SubjectServiceTrait
import scala.collection.JavaConversions._

class SubjectService extends SubjectServiceTrait {
  @Autowired
  private val repository: SubjectRepository = null

  def create(): Subject = Subject()

  def getAllSubjects = repository.findAll().as(classOf[java.util.List[Subject]])

  def findSubjectById(subjectId: Long) = repository.findOne(subjectId)

  def searchSubjectsByKeyword(keyword: String) = List[Subject]()

  def addSubject(subject: Subject) = repository.save(subject)

  def deleteSubject(subjectId: Long) {
    repository.delete(subjectId)
  }

  def prependSubjectListener(f: (Subject) => Unit) {}
}
