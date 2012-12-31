package org.tscore.web.lib.controller

import net.liftweb.common.{Empty, Full, Box}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.tscore.trust.service.ActorServiceTrait
import scala.Some
import org.tscore.web.model.Actor
import org.tscore.trust.model.score.ActorScore

class ActorController {}

@Controller
object ActorController {
  private implicit val formats = net.liftweb.json.DefaultFormats

  @Autowired
  private var actorService: ActorServiceTrait = null

  implicit def doubleToActorScore(in: Double): ActorScore = new ActorScore(in)

  implicit def doubleOptionToActorScoreOption(in: Option[Double]): Option[ActorScore] = in match {
    case Some(s) => Some(doubleToActorScore(s))
    case None => None
  }

  implicit def toActor(in: org.tscore.trust.model.Actor): Actor = {
    Actor(Option(in.id), in.name, Option(in.description), Option(in.score))
  }

  implicit def toActorSeq(in: Seq[org.tscore.trust.model.Actor]): Seq[Actor] = {
    in.map(toActor)
  }

  implicit def toActorBox(in: Option[org.tscore.trust.model.Actor]): Box[Actor] = {
    in match {
      case Some(a) => Full(toActor(a))
      case None => Empty }
  }

  implicit def fromActor(in: Actor): org.tscore.trust.model.Actor = {
    actorService.createActor(in.id, in.name, in.description, in.score).get
  }

  def allActors: Seq[Actor] = actorService.getAllActors

  def find(id: Long): Box[Actor] = {
    actorService.findActorById(id)
  }

  def search(str: String): Seq[Actor] = actorService.searchActorsByKeyword(str)

  def add(subject: Actor): Box[Actor] = {
    actorService.addActor(subject)
  }

  def delete(id: Long): Box[Actor] = actorService.deleteActor(id)

  def onChange(f: Actor => Unit) {
    synchronized {
      def g(actor: org.tscore.trust.model.Actor) {
        f(actor)
      }
      actorService.prependActorListener(g)
    }
  }
}