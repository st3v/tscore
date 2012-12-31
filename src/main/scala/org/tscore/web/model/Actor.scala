package org.tscore.web.model

import net.liftweb._
import common._
import json._
import scala.xml.Node
import org.tscore.web.lib.controller.ActorController
import util.Helpers
import scala.util.control.Exception._
import scala.Some

//A subject among subjects
case class Actor(id: Option[Long] = None,
                 name: String,
                 description: Option[String] = None,
                 score: Option[Double] = None) {
}

//The Subject companion object
object Actor {
  private implicit val formats = net.liftweb.json.DefaultFormats

  /**
   * Convert a JValue to a Subject if possible
   */
  def apply(in: JValue): Option[Actor] = catching(classOf[Exception]).opt(in.extract[Actor])


  /**
   * Extract a Long (id) to a Subject
   */
  def unapply(id: Long): Option[Actor] = ActorController.find(id)

  /**
   * Extract a JValue to a Subject
   */
  def unapply(in: JValue): Option[Actor] = {
    apply(in)
  }

  def unapply(in: Any): Option[(Option[Long], String, Option[String], Option[Double])] = {
    in match {
      case a: Actor => Some((a.id, a.name, a.description, a.score))
      case _ => None
    }
  }

  implicit def toXml(actor: Actor): Node =
    <subject>{Xml.toXml(actor)}</subject>


  implicit def toJson(actor: Actor): JValue =
    Extraction.decompose(actor)

  implicit def toJson(actors: Seq[Actor]): JValue =
    Extraction.decompose(actors)

  implicit def toXml(actors: Seq[Actor]): Node =
    <subjects>{
      actors.map(toXml)
      }</subjects>
}
