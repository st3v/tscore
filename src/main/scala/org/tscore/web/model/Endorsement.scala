package org.tscore.web.model

import net.liftweb._
import util._
import common._
import json._
import scala.xml.Node
import org.tscore.web.lib.controller.EndorsementController
import scala.util.control.Exception._
import scala.Some

//An endorsement among endorsements
case class Endorsement(id: Option[java.lang.Long], actorId: Long, subjectId: Long, score: Option[Int])

//The Endorsement companion object
object Endorsement {
  private implicit val formats = net.liftweb.json.DefaultFormats

  /**
   * Convert a JValue to a Endorsement if possible
   */
  def apply(in: JValue): Option[Endorsement] = catching(classOf[Exception]).opt(in.extract[Endorsement])

  /**
   * Extract a Long (id) to a Endorsement
   */
  def unapply(id: BigInt): Option[Endorsement] = EndorsementController.find(id.toLong)

  /**
   * Extract a JValue to a Endorsement
   */
  def unapply(in: JValue): Option[Endorsement] = apply(in)

  /**
   * The default unapply method for the case class.
   * We needed to replicate it here because we
   * have overloaded unapply methods
   */
  def unapply(in: Any): Option[(Option[java.lang.Long], Long, Long, Option[Int])] = {
    in match {
      case s: Endorsement => Some((s.id, s.actorId, s.subjectId, s.score))
      case _ => None
    }
  }

  /**
   * Convert a endorsement to XML
   */
  implicit def toXml(endorsement: Endorsement): Node =
    <endorsement>{Xml.toXml(endorsement)}</endorsement>


  /**
   * Convert the endorsement to JSON format.  This is
   * implicit and in the companion object, so
   * a Endorsement can be returned easily from a JSON call
   */
  implicit def toJson(endorsement: Endorsement): JValue =
    Extraction.decompose(endorsement)

  /**
   * Convert a Seq[Endorsement] to JSON format.  This is
   * implicit and in the companion object, so
   * a Endorsement can be returned easily from a JSON call
   */
  implicit def toJson(endorsements: Seq[Endorsement]): JValue =
    Extraction.decompose(endorsements)

  /**
   * Convert a Seq[Endorsement] to XML format.  This is
   * implicit and in the companion object, so
   * a Endorsement can be returned easily from an XML REST call
   */
  implicit def toXml(endorsements: Seq[Endorsement]): Node =
    <endorsements>{
      endorsements.map(toXml)
      }</endorsements>

}