package org.tscore.web.model

import net.liftweb._
import common._
import json._
import scala.xml.Node
import org.tscore.web.lib.controller.SubjectController
import util.Helpers

//A subject among subjects
case class Subject(id: Option[Long] = None,
                   name: String,
                   description: Option[String] = None) {
}

//The Subject companion object
object Subject {
  private implicit val formats = net.liftweb.json.DefaultFormats

  /**
   * Convert a JValue to a Subject if possible
   */
  def apply(in: JValue): Box[Subject] = Helpers.tryo({in.extract[Subject]})


  /**
   * Extract a Long (id) to a Subject
   */
  def unapply(id: Long): Option[Subject] = SubjectController.find(id)

  /**
   * Extract a JValue to a Subject
   */
  def unapply(in: JValue): Option[Subject] = {
    apply(in)
  }

  /**
   * The default unapply method for the case class.
   * We needed to replicate it here because we
   * have overloaded unapply methods
   */
  def unapply(in: Any): Option[(Option[Long], String, Option[String])] = {
    in match {
      case s: Subject => Some((s.id, s.name, s.description))
      case _ => None
    }
  }

  /**
   * Convert a subject to XML
   */
  implicit def toXml(subject: Subject): Node =
    <subject>{Xml.toXml(subject)}</subject>


  /**
   * Convert the subject to JSON format.  This is
   * implicit and in the companion object, so
   * a Subject can be returned easily from a JSON call
   */
  implicit def toJson(subject: Subject): JValue =
    Extraction.decompose(subject)

  /**
   * Convert a Seq[Subject] to JSON format.  This is
   * implicit and in the companion object, so
   * a Subject can be returned easily from a JSON call
   */
  implicit def toJson(subjects: Seq[Subject]): JValue =
    Extraction.decompose(subjects)

  /**
   * Convert a Seq[Subject] to XML format.  This is
   * implicit and in the companion object, so
   * a Subject can be returned easily from an XML REST call
   */
  implicit def toXml(subjects: Seq[Subject]): Node =
    <subjects>{
      subjects.map(toXml)
      }</subjects>
}
