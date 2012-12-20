package org.tscore.api.lib

import net.liftweb._
import http._
import rest._
import util._
import Helpers._
import json._
import org.tscore.api.model.Subject


//https://github.com/dpp/simply_lift/blob/master/samples/http_rest/src/main/scala/code/lib/FullRest.scala

object TScoreREST extends RestHelper {

  // Serve /api/subject and friends
  //Testing route: http://localhost:8080/api/subject/XXX
  serve( "api" / "subject" prefix {

    // /api/subject returns all the subjects
    case Nil JsonGet _ => Subject.allSubjects: JValue

    // /api/subject/count gets the subject count
    case "count" :: Nil JsonGet _ => JInt(Subject.allSubjects.length)

    // /api/subject/subject_id gets the specified subject (or a 404)
    case Subject(subject) :: Nil JsonGet _ => subject: JValue

    // /api/subject/search/foo or /api/subject/search?q=foo
    case "search" :: q JsonGet _ =>
      (for {
        searchString <- q ::: S.params("q")
        subject <- Subject.search(searchString)
      } yield subject).distinct: JValue

    // DELETE the subject in question
    case Subject(subject) :: Nil JsonDelete _ =>
      Subject.delete(subject.id).map(a => a: JValue)

    // PUT adds the subject if the JSON is parsable
    case Nil JsonPut Subject(subject) -> _ => Subject.add(subject): JValue

    // POST if we find the subject, merge the fields from the
    // the POST body and update the subject
    //TODO: Figure out how to make this work
    /**
    case Subject(subject) :: Nil JsonPost json -> _ =>
      Subject(mergeJson(subject, json)).map(Subject.add(_): JValue)

    */
  })
}