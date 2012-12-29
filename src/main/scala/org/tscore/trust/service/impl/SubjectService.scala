package org.tscore.trust.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.tscore.trust.repository.SubjectRepository
import org.tscore.trust.model.Subject
import org.tscore.trust.service.SubjectServiceTrait
import scala.collection.JavaConversions._

class SubjectService extends SubjectServiceTrait {
  @Autowired
  private val repository: SubjectRepository = null

  def createSubject(id: Option[Long], name: String, description: Option[String]): Option[Subject] = {
    Option(Subject(id.getOrElse(null.asInstanceOf[Long]), name, description.getOrElse(null)))
  }

  def getAllSubjects: Seq[Subject] = repository.findAll().as(classOf[java.util.List[Subject]])

  def findSubjectById(subjectId: Long): Option[Subject] = Option(repository.findOne(subjectId))

  def searchSubjectsByKeyword(keyword: String) = List[Subject]()

  def addSubject(subject: Subject): Option[Subject] = {
    if (subject.id != null && !repository.exists(subject.id)) {
      subject.id = null
    }
    Option(repository.save(subject))
  }

  def deleteSubject(subjectId: Long): Option[Subject] = {
    findSubjectById(subjectId) match {
      case Some(subject) => {
        repository.delete(subjectId)
        Some(subject)
      }
      case None => None
    }
  }

  def prependSubjectListener(f: (Subject) => Unit) {}
}
