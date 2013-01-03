package org.tscore.trust.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.tscore.trust.model.score.ActorScore
import org.tscore.trust.model.Actor
import org.tscore.trust.service.ActorService
import org.tscore.trust.service.repository.ActorRepository

class ActorServiceImpl extends ActorService with CrudServiceImpl[Actor] with SearchServiceImpl[Actor] {
  @Autowired
  override protected val repository: ActorRepository = null

  def create(id: Option[java.lang.Long],
            name: String,
            description: Option[String],
            score: Option[ActorScore]): Option[Actor] =
    Option(Actor(id.getOrElse(null), name, description.getOrElse(null), score.getOrElse(null)))

  def find(name: String): Option[Actor] = Option(repository.findByName(name))
}
