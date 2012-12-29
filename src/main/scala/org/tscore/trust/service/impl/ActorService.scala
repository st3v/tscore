package org.tscore.trust.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.tscore.trust.repository.ActorRepository
import org.tscore.trust.model.score.ActorScore
import org.tscore.trust.model.Actor
import org.tscore.trust.service.ActorServiceTrait
import scala.collection.JavaConversions._

class ActorService extends ActorServiceTrait {
  @Autowired
  private val repository: ActorRepository = null

  def createActor(id: Option[Long],
                  name: String,
                  description: Option[String],
                  score: Option[ActorScore]): Option[Actor] =
    Option(Actor(id.getOrElse(null.asInstanceOf[Long]), name, description.getOrElse(null), score.getOrElse(null)))

  def getAllActors = repository.findAll().as(classOf[java.util.List[Actor]])

  def findActorById(actorId: Long): Option[Actor] = Option(repository.findOne(actorId))

  def findActorByName(actorName: String): Option[Actor] = Option(repository.findByName(actorName))

  def searchActorsByKeyword(keyword: String): List[Actor] = List[Actor]()

  def addActor(actor: Actor): Option[Actor] = {
    if (actor.id != null && !repository.exists(actor.id)) {
      actor.id = null
    }
    Option(repository.save(actor))
  }

  def deleteActor(actorId: Long): Option[Actor] = {
    findActorById(actorId) match {
      case Some(actor) => {
        repository.delete(actorId)
        Some(actor)
      }
      case None => None
    }
  }

  def prependActorListener(f: (Actor) => Unit) {}
}
