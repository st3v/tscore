package org.tscore.web.lib.controller

import net.liftweb.common.{Empty, Full, Box}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.tscore.trust.service.SubjectServiceTrait
import scala.Some
import org.tscore.web.model.Subject

class SubjectController {}

@Controller
object SubjectController {
  private implicit val formats = net.liftweb.json.DefaultFormats

  @Autowired
  private var subjectService: SubjectServiceTrait = null

  implicit def toSubject(in: org.tscore.trust.model.Subject): Subject = {
    Subject(Option(in.id), in.name, Option(in.description))
  }

  implicit def toSubjectSeq(in: Seq[org.tscore.trust.model.Subject]): Seq[Subject] = {
    in.map(toSubject)
  }

  implicit def toSubjectBox(in: Option[org.tscore.trust.model.Subject]): Box[Subject] = {
    in match {
      case Some(s) => Full(toSubject(s))
      case None => Empty }
  }

  implicit def fromSubject(in: Subject): org.tscore.trust.model.Subject = {
    subjectService.createSubject(in.id, in.name, in.description).get
  }

  //Get all subjects
  def allSubjects: Seq[Subject] = subjectService.getAllSubjects

  //Find a Subject by ID
  def find(id: Long): Box[Subject] = {
    subjectService.findSubjectById(id)
  }

  //Find all the subjects with the string in their name or description
  def search(str: String): Seq[Subject] = subjectService.searchSubjectsByKeyword(str)

  //Add a subject
  def add(subject: Subject): Box[Subject] = {
    subjectService.addSubject(subject)
  }

  //Deletes the subject with id and returns the deleted subject or Empty if there's no match
  def delete(id: Long): Box[Subject] = subjectService.deleteSubject(id)

  //Add an onChange listener
  def onChange(f: Subject => Unit) {
    synchronized {
      def g(subject: org.tscore.trust.model.Subject) {
        f(subject)
      }
      subjectService.prependSubjectListener(g)
    }
  }

}