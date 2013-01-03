package org.tscore.trust.service

import org.tscore.trust.model.Subject

trait SubjectService extends CrudService[Subject] with SearchService[Subject] {
  def create(id: Option[java.lang.Long], name: String, description: Option[String]): Option[Subject]
}
