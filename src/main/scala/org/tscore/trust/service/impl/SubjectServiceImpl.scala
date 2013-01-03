package org.tscore.trust.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.tscore.trust.service.repository.SubjectRepository
import org.tscore.trust.model.Subject
import org.tscore.trust.service.SubjectService

class SubjectServiceImpl extends SubjectService with CrudServiceImpl[Subject] with SearchServiceImpl[Subject] {
  @Autowired
  override protected val repository: SubjectRepository = null

  def create(id: Option[java.lang.Long], name: String, description: Option[String]): Option[Subject] = {
    Option(Subject(id.getOrElse(null), name, description.getOrElse(null)))
  }
}
