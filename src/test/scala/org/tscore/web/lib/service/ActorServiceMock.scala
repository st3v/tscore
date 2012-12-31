package org.tscore.web.lib.service

import net.liftweb._
import util._
import Helpers._

import net.liftweb.util.Schedule
import org.tscore.trust.service.ActorServiceTrait
import org.tscore.trust.model.Actor
import org.tscore.trust.model.score.ActorScore

class ActorServiceMock extends ActorServiceTrait {

  var actors: List[Actor] = Nil

  var listeners: List[Actor => Unit] = Nil

  def createActor(id: Option[Long],
                  name: String,
                  description: Option[String],
                  score: Option[ActorScore]): Option[Actor] = Option(
    Actor(
      id.getOrElse(null.asInstanceOf[Long]),
      name,
      description.getOrElse(null),
      score.getOrElse(null.asInstanceOf[ActorScore])
    )
  )

  def getAllActors: Seq[Actor] = {
    actors
  }

  def findActorById(actorId: Long): Option[Actor] = synchronized {
    actors.find(_.id == actorId)
  }

  def findActorByName(actorName: String) = synchronized {
    actors.find(_.name == actorName)
  }

  def searchActorsByKeyword(keyword: String): Seq[Actor] = {
    val keywordLC = keyword.toLowerCase
    actors.filter(i => i.name.toLowerCase.indexOf(keywordLC) >= 0 || i.description.toLowerCase.indexOf(keywordLC) >= 0)
  }

  def addActor(actor: Actor): Option[Actor] = synchronized {
    var result: Actor = null
    if (findActorById(actor.id).isEmpty) {
      actors ::= actor
      updateListeners(actor)
      result = actor
    }
    Option(result)
  }

  def deleteActor(actorId: Long): Option[Actor] = synchronized {
    val actor: Option[Actor] = findActorById(actorId)
    if (actor.isDefined) {
      actors = actors.diff(List(actor.get))
      updateListeners(actor.get)
    }
    actor
  }

  def prependActorListener(f: Actor => Unit) {
    listeners ::= f
  }

  private def updateListeners(actor: Actor) = {
    synchronized {
      listeners.foreach(f =>
        Schedule.schedule(() => f(actor), 0 seconds))

      listeners = Nil
    }
    actor
  }
}
