package org.tscore.trust.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.tscore.trust.repository.ActorRepository
import org.tscore.trust.model.score.TrustScore
import org.tscore.trust.model.Actor
import org.tscore.trust.service.ActorServiceTrait
import scala.collection.JavaConversions._

class ActorService extends ActorServiceTrait {
  @Autowired
  private val repository: ActorRepository = null

  def create(name: String, score: TrustScore = TrustScore.zero): Actor = Actor(name, score)

  def getAllActors = repository.findAll().as(classOf[java.util.List[Actor]])

  def findActorById(actorId: Long): Actor = repository.findOne(actorId)

  def findActorByName(actorName: String): Actor = repository.findByName(actorName)

  def searchActorsByKeyword(keyword: String): List[Actor] = List[Actor]()

  def addActor(actor: Actor): Actor = repository.save(actor)

  def deleteActor(actorId: Long) {
    repository.delete(actorId)
  }

  def prependActorListener(f: (Actor) => Unit) {}
}
