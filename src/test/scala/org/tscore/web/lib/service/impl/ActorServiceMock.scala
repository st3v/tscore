package org.tscore.web.lib.service.impl

import org.tscore.trust.service.ActorService
import org.tscore.trust.model.Actor
import org.tscore.trust.model.score.ActorScore
import org.tscore.web.lib.service.ServiceMock

class ActorServiceMock extends ServiceMock[Actor] with ActorService {

  def create(id: Option[java.lang.Long],
             name: String,
             description: Option[String],
             score: Option[ActorScore]): Option[Actor] = Option(
    Actor(
      id.getOrElse(null),
      name,
      description.getOrElse(null),
      score.getOrElse(null.asInstanceOf[ActorScore])
    )
  )

  def find(actorName: String) = synchronized {
    repository.values.find(_.name == actorName)
  }

  def search(keyword: String): Seq[Actor] = {
    val keywordLC = keyword.toLowerCase
    repository.values.filter(i => i.name.toLowerCase.indexOf(keywordLC) >= 0 || i.description.toLowerCase.indexOf(keywordLC) >= 0).toSeq
  }
}
