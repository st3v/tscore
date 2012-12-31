package org.tscore.web.lib.service

import net.liftweb._
import util._
import Helpers._

import net.liftweb.util.Schedule
import org.tscore.trust.service.SubjectServiceTrait
import org.tscore.trust.model.Subject

class SubjectServiceMock extends SubjectServiceTrait {

  var subjects: List[Subject] = Nil

  var listeners: List[Subject => Unit] = Nil

  def createSubject(id: Option[Long],
                    name: String,
                    description: Option[String]): Option[Subject] = {
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
      subjects ::= subject
      updateListeners(subject)
      result = subject
    }
    Option(result)
  }

  def deleteSubject(subjectId: Long): Option[Subject] = synchronized {
    val subject: Option[Subject] = findSubjectById(subjectId)
    if (subject.isDefined) {
      subjects = subjects.diff(List(subject.get))
      updateListeners(subject.get)
    }
    subject
  }

  def prependSubjectListener(f: Subject => Unit) {
    listeners ::= f
  }

  private def updateListeners(subject: Subject) = {
    synchronized {
      listeners.foreach(f =>
        Schedule.schedule(() => f(subject), 0 seconds))

      listeners = Nil
    }
    subject
  }
}
