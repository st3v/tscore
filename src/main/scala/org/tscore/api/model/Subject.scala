package org.tscore.api.model

import net.liftweb._
import util._
import Helpers._
import common._
import json._
import scala.xml.Node

//A subject among subjects
case class Subject(id: String, name: String, description: String, loanQuantity: BigDecimal)

//The Subject companion object
object Subject {
  private implicit val formats = net.liftweb.json.DefaultFormats + BigDecimalSerializer

  private var subjects = parse(data).extract[List[Subject]]

  private var listeners: List[Subject => Unit] = Nil

  //def apply() = new Subject()

  /**
   * Convert a JValue to a Subject if possible
   */
  def apply(in: JValue): Box[Subject] = Helpers.tryo{in.extract[Subject]}

  /**
   * Extract a String (id) to a Subject
   */
  def unapply(id: String): Option[Subject] = Subject.find(id)

  /**
   * Extract a JValue to a Subject
   */
  def unapply(in: JValue): Option[Subject] = apply(in)

  /**
   * The default unapply method for the case class.
   * We needed to replicate it here because we
   * have overloaded unapply methods
   */
  def unapply(in: Any): Option[(String, String,
    String,
    BigDecimal)] = {
    in match {
      case s: Subject => Some((s.id, s.name, s.description, s.loanQuantity))
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


  //Get all subjects
  def allSubjects: Seq[Subject] = subjects

  // The raw data
  private def data =
  """[
  {
    "id": "1234",
    "name": "Tim the Baker",
    "description": "Needs a Kiva Zip loan to bake goodies",
    "loanQuantity": 200
  },
  {
    "id": "4567",
    "name": "Jeremy the social worker",
    "description": "Needs a Kiva Zip loan to help his community",
    "loanQuantity": 800
  },
  ]"""

  //Find a Subject by ID
  def find(id: String): Box[Subject] = synchronized {
    subjects.find(_.id == id)
  }

  //Find all the subjects with the string in their name or description
  def search(str: String): List[Subject] = {
    val strLC = str.toLowerCase()

    subjects.filter(i => i.name.toLowerCase.indexOf(strLC) >= 0 || i.description.toLowerCase.indexOf(strLC) >= 0)
  }

  //Add a subject
  def add(subject: Subject): Subject = {
    synchronized {
      subjects = subject :: subjects.filterNot(_.id == subject.id)
      updateListeners(subject)
    }
  }

  //Deletes the subject with id and returns the deleted subject or Empty if there's no match
  def delete(id: String): Box[Subject] = synchronized {
    var ret: Box[Subject] = Empty

    val Id = id   //an upper case stable ID for pattern matching

    subjects = subjects.filter {
      case i@Subject(Id, _, _, _) =>
        ret = Full(i) // side effect
        false
      case _ => true
    }

    ret.map(updateListeners)
  }

  //Update listeners when the data changes
  private def updateListeners(subject: Subject): Subject = {
    synchronized {
      listeners.foreach(f =>
        Schedule.schedule(() => f(subject), 0 seconds))

      listeners = Nil
    }
    subject
  }

  //Add an onChange listener
  def onChange(f: Subject => Unit) {
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
