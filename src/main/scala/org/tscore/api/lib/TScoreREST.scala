package org.tscore.api.lib

import net.liftweb._
import http._
import rest._
import util._
import json._
import org.tscore.api.model.Subject
import org.tscore.api.model.Endorsement

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

  // Serve /api/endorsement and friends
  //Testing route: http://localhost:8080/api/endorsement/XXX
  serve( "api" / "endorsement" prefix {

    // /api/endorsement returns all the endorsements
    case Nil JsonGet _ => Endorsement.allEndorsements: JValue

    // /api/endorsement/count gets the endorsement count
    case "count" :: Nil JsonGet _ => JInt(Endorsement.allEndorsements.length)

    // /api/endorsement/endorsement_id gets the specified endorsement (or a 404)
    case Endorsement(endorsement) :: Nil JsonGet _ => endorsement: JValue

    // /api/endorsement/search/foo or /api/endorsement/search?q=foo
    case "search" :: q JsonGet _ =>
      (for {
        searchString <- q ::: S.params("q")
        endorsement <- Endorsement.search(searchString)
      } yield endorsement).distinct: JValue

    // DELETE the endorsement in question
    case Endorsement(endorsement) :: Nil JsonDelete _ =>
      Endorsement.delete(endorsement.id).map(a => a: JValue)

    // PUT adds the endorsement if the JSON is parsable
    case Nil JsonPut Endorsement(endorsement) -> _ => Endorsement.add(endorsement): JValue

    // POST if we find the endorsement, merge the fields from the
    // the POST body and update the endorsement
    //TODO: Figure out how to make this work
    /**
    case Endorsement(endorsement) :: Nil JsonPost json -> _ =>
      Endorsement(mergeJson(endorsement, json)).map(Endorsement.add(_): JValue)

      */
  })
}