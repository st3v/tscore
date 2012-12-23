package org.tscore.trust

import org.springframework.beans.factory.annotation.Autowired
import org.tscore.graph.repository.ActorRepository
import org.tscore.trust.score.TrustScore

class TrustService {

  @Autowired
  private val actorRepository: ActorRepository = null

  private def safeCast[T : Manifest](instance: AnyRef) : T = {
    if (Manifest.singleType(instance) <:< manifest[T]) {
      instance.asInstanceOf[T]
    }
    else {
      null.asInstanceOf[T]
    }
  }

  implicit def toTrustActor[U <: AnyRef](obj: U): TrustActor = safeCast[TrustActor](obj)

  def createActor(name: String): TrustActor = TrustActor(name)

  def createActor(name: String, score: TrustScore): TrustActor = TrustActor(name, score)

  def findActor(id: Long): TrustActor = actorRepository.findActorById(id)

  def findActor(name: String): TrustActor = actorRepository.findActorByName(name)

  def saveActor(actor: TrustActor): TrustActor = actorRepository.save(actor)

}
