package org.tscore.api.model

import net.liftweb._
import util._
import Helpers._
import common._
import json._
import scala.xml.Node

//An endorsement among endorsements
case class Endorsement(id: String, description: String, size: BigDecimal)

//The Endorsement companion object
object Endorsement {
  private implicit val formats = net.liftweb.json.DefaultFormats + BigDecimalSerializer

  private var endorsements = parse(data).extract[List[Endorsement]]

  private var listeners: List[Endorsement => Unit] = Nil

  //def apply() = new Endorsement()

  /**
   * Convert a JValue to a Endorsement if possible
   */
  def apply(in: JValue): Box[Endorsement] = Helpers.tryo{in.extract[Endorsement]}

  /**
   * Extract a String (id) to an Endorsement
   */
  def unapply(id: String): Option[Endorsement] = Endorsement.find(id)

  /**
   * Extract a JValue to an Endorsement
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
   * Convert an endorsement to XML
   */
  implicit def toXml(endorsement: Endorsement): Node =
    <endorsement>{Xml.toXml(endorsement)}</endorsement>


  /**
   * Convert the endorsement to JSON format.  This is
   * implicit and in the companion object, so
   * an Endorsement can be returned easily from a JSON call
   */
  implicit def toJson(endorsement: Endorsement): JValue =
    Extraction.decompose(endorsement)

  /**
   * Convert a Seq[Endorsement] to JSON format.  This is
   * implicit and in the companion object, so
   * an Endorsement can be returned easily from a JSON call
   */
  implicit def toJson(endorsements: Seq[Endorsement]): JValue =
    Extraction.decompose(endorsements)

  /**
   * Convert a Seq[Endorsement] to XML format.  This is
   * implicit and in the companion object, so
   * an Endorsement can be returned easily from an XML REST call
   */
  implicit def toXml(endorsements: Seq[Endorsement]): Node =
    <endorsements>{
      endorsements.map(toXml)
      }</endorsements>


  //Get all endorsements
  def allEndorsements: Seq[Endorsement] = endorsements

  // The raw data
  private def data =
    """[
  {
    "id": "1",
    "description": "Made a repayment",
    "size": 20
  },
  {
    "id": "2",
    "description": "Had a recommendation make a repayment",
    "size": 10
  },
  ]"""

  //Find an Endorsement by ID
  def find(id: String): Box[Endorsement] = synchronized {
    endorsements.find(_.id == id)
  }

  //Find all the endorsements with the string in their description
  def search(str: String): List[Endorsement] = {
    val strLC = str.toLowerCase()

    endorsements.filter(i => i.description.toLowerCase.indexOf(strLC) >= 0)
  }

  //Add an endorsement
  def add(endorsement: Endorsement): Endorsement = {
    synchronized {
      endorsements = endorsement :: endorsements.filterNot(_.id == endorsement.id)
      updateListeners(endorsement)
    }
  }

  //Deletes the endorsement with id and returns the deleted endorsement or Empty if there's no match
  def delete(id: String): Box[Endorsement] = synchronized {
    var ret: Box[Endorsement] = Empty

    val Id = id   //an upper case stable ID for pattern matching

    endorsements = endorsements.filter {
      case i@Endorsement(Id, _, _) =>
        ret = Full(i) // side effect
        false
      case _ => true
    }

    ret.map(updateListeners)
  }

  //Update listeners when the data changes
  private def updateListeners(endorsement: Endorsement): Endorsement = {
    synchronized {
      listeners.foreach(f =>
        Schedule.schedule(() => f(endorsement), 0 seconds))

      listeners = Nil
    }
    endorsement
  }

  //Add an onChange listener
  def onChange(f: Endorsement => Unit) {
    synchronized {
      //prepend the function to the list of listeners
      listeners ::= f
    }
  }

  /**
   * A helper that will JSON serialize BigDecimal
   */
  object BigDecimalSerializer extends Serializer[BigDecimal] {
    private val Class = classOf[BigDecimal]

    def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), BigDecimal] = {
      case (TypeInfo(Class, _), json) => json match {
        case JInt(iv) => BigDecimal(iv)
        case JDouble(dv) => BigDecimal(dv)
        case value => throw new MappingException("Can't convert " + value + " to " + Class)
      }
    }

    def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
      case d: BigDecimal => JDouble(d.doubleValue)
    }
  }

}
