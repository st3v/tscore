package org.tscore.web.lib.service.impl

import net.liftweb._
import util._
import Helpers._

import net.liftweb.util.Schedule
import org.tscore.trust.service.SubjectService
import org.tscore.trust.model.Subject
import org.tscore.web.lib.service.ServiceMock

class SubjectServiceMock extends ServiceMock[Subject] with SubjectService {

  def create(id: Option[java.lang.Long],
            name: String,
            description: Option[String]): Option[Subject] = {
    Option(Subject(id.getOrElse(null), name, description.getOrElse(null)))
  }

  def search(keyword: String): Seq[Subject] = {
    val keywordLC = keyword.toLowerCase
    repository.values.filter(i => i.name.toLowerCase.indexOf(keywordLC) >= 0 || i.description.toLowerCase.indexOf(keywordLC) >= 0).toSeq
  }
}
