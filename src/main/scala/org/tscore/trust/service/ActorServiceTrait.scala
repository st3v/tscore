package org.tscore.trust.service

import org.tscore.trust.model.Actor

trait ActorServiceTrait {
  def getAllActors: Seq[Actor]
  def findActorById(actorId: Long): Actor
  def findActorByName(actorName: String): Actor
  def searchActorsByKeyword(keyword: String): Seq[Actor]
  def addActor(actor: Actor): Actor
  def deleteActor(actorId: Long)
  def prependActorListener(f: Actor => Unit)

}
