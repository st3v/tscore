package org.tscore.api.model

import net.liftweb._
import util._
import common._
import json._
import scala.xml.Node
import org.tscore.api.snippet.TrustRetrievalMock
import org.tscore.api.snippet.BigDecimalSerializer

//An endorsement among endorsements
case class Endorsement(id: String, description: String, size: BigDecimal)

//The Endorsement companion object
object Endorsement {
  private implicit val formats = net.liftweb.json.DefaultFormats + BigDecimalSerializer
  private val tr = new TrustRetrievalMock

  /**
   * Convert a JValue to a Endorsement if possible
   */
  def apply(in: JValue): Box[Endorsement] = Helpers.tryo{in.extract[Endorsement]}

  /**
   * Extract a String (id) to a Endorsement
   */
  def unapply(id: String): Option[Endorsement] = Endorsement.find(id)

  /**
   * Extract a JValue to a Endorsement
   */
  def unapply(in: JValue): Option[Endorsement] = apply(in)

  /**
   * The default unapply method for the case class.
   * We needed to replicate it here because we
   * have overloaded unapply methods
   */
  def unapply(in: Any): Option[(String, String, BigDecimal)] = {
    in match {
      case s: Endorsement => Some((s.id, s.description, s.size))
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


  //Get all endorsements
  def allEndorsements: List[Endorsement] = tr.getAllEndorsements()

  //Find a Endorsement by ID
  def find(id: String): Box[Endorsement] = tr.findEndorsementById(id)

  //Find all the endorsements with the string in their name or description
  def search(str: String): List[Endorsement] = tr.searchEndorsementsByKeyword(str)

  //Add a endorsement
  def add(endorsement: Endorsement): Endorsement = tr.addEndorsement(endorsement)

  //Deletes the endorsement with id and returns the deleted endorsement or Empty if there's no match
  def delete(id: String): Box[Endorsement] = tr.deleteEndorsement(id)

  //Add an onChange listener
  def onChange(f: Endorsement => Unit) {
    synchronized {
      tr.prependEndorsementListener(f)
    }
  }



}