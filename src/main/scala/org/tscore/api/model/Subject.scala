package org.tscore.api.model

import net.liftweb._
import util._
import common._
import json._
import scala.xml.Node
import org.tscore.api.snippet.TrustRetrievalMock
import org.tscore.api.snippet.BigDecimalSerializer

//A subject among subjects
case class Subject(id: String, name: String, description: String, loanQuantity: BigDecimal)

//The Subject companion object
object Subject {
  private implicit val formats = net.liftweb.json.DefaultFormats + BigDecimalSerializer
  private val tr = new TrustRetrievalMock

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
  def allSubjects: List[Subject] = tr.getAllSubjects()

  //Find a Subject by ID
  def find(id: String): Box[Subject] = tr.findSubjectById(id)

  //Find all the subjects with the string in their name or description
  def search(str: String): List[Subject] = tr.searchSubjectsByKeyword(str)

  //Add a subject
  def add(subject: Subject): Subject = tr.addSubject(subject)

  //Deletes the subject with id and returns the deleted subject or Empty if there's no match
  def delete(id: String): Box[Subject] = tr.deleteSubject(id)

  //Add an onChange listener
  def onChange(f: Subject => Unit) {
    synchronized {
      tr.prependSubjectListener(f)
    }
  }



}
