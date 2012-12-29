package org.tscore.trust.service

import org.tscore.trust.model.Actor
import org.tscore.trust.model.score.ActorScore

trait ActorServiceTrait {
  def createActor(id: Option[Long], name: String, description: Option[String], score: Option[ActorScore]): Option[Actor]
  def getAllActors: Seq[Actor]
  def findActorById(actorId: Long): Option[Actor]
  def findActorByName(actorName: String): Option[Actor]
  def searchActorsByKeyword(keyword: String): Seq[Actor]
  def addActor(actor: Actor): Option[Actor]
  def deleteActor(actorId: Long): Option[Actor]
  def prependActorListener(f: Actor => Unit)
}
