package org.tscore.web.lib.api

import net.liftweb.http.rest.RestHelper
import net.liftweb.http.S
import net.liftweb.json.JInt
import net.liftweb.json.JsonAST.JValue
import org.tscore.web.model.{Actor, Subject, Endorsement}
import org.tscore.web.lib.controller.{ActorController, EndorsementController, SubjectController}
import net.liftweb.common.Empty
import net.liftweb.util.Helpers

object RestRules extends RestHelper {

  // Serve /api/subject and friends
  //Testing route: http://localhost:8080/api/subject/XXX
  serve( "api" / "subject" prefix {

    // /api/subject returns all the subjects
    case Nil JsonGet _ => {
      SubjectController.allSubjects: JValue
    }

    // /api/subject/count gets the subject count
    case "count" :: Nil JsonGet _ => JInt(SubjectController.allSubjects.length)

    // /api/subject/search/foo or /api/subject/search?q=foo
    case "search" :: q JsonGet _ =>
      (for {
        searchString <- q ::: S.params("q")
        subject <- SubjectController.search(searchString)
      } yield subject).distinct: JValue

    // /api/subject/subject_id gets the specified subject (or a 404)
    case id :: Nil JsonGet _ => Helpers.tryo({
      SubjectController.find(id.toLong)
    }).getOrElse(Empty).map(a => a: JValue)

    // DELETE the subject in question
    case id :: Nil JsonDelete _ =>  Helpers.tryo({
      SubjectController.delete(id.toLong)
    }).getOrElse(Empty).map(a => a: JValue)

    // PUT adds the subject if the JSON is parsable
    case Nil JsonPut Subject(json) -> _ => {
      SubjectController.add(json).get: JValue
    }

    // POST if we find the subject, merge the fields from the
    // the POST body and update the subject
    //TODO: Use true UPDATE instead of DELETE/ADD sequence
    case id :: Nil JsonPost json -> _ => {
      var subject = Helpers.tryo({SubjectController.delete(id.toLong).get})
      if (subject.isDefined) {
        subject = Subject(
          mergeJson(subject.get, json)).map(SubjectController.add(_).get
        )
      }
      subject.getOrElse(null) : JValue
    }
  })

  // Serve /api/actor and friends
  //Testing route: http://localhost:8080/api/actor/XXX
  serve( "api" / "actor" prefix {

    // GET /
    case Nil JsonGet _ => {
      ActorController.allActors: JValue
    }

    // GET /count
    case "count" :: Nil JsonGet _ => JInt(ActorController.allActors.length)

    // GET /search
    case "search" :: q JsonGet _ =>
      (for {
        searchString <- q ::: S.params("q")
        actor <- ActorController.search(searchString)
      } yield actor).distinct: JValue

    // GET /<id>
    case id :: Nil JsonGet _ => Helpers.tryo({
      ActorController.find(id.toLong)
    }).getOrElse(Empty).map(a => a: JValue)

    // DELETE /<id>
    case id :: Nil JsonDelete _ =>  Helpers.tryo({
      ActorController.delete(id.toLong)
    }).getOrElse(Empty).map(a => a: JValue)

    // PUT
    case Nil JsonPut Actor(json) -> _ => {
      ActorController.add(json).get: JValue
    }

    // POST /<id>
    //TODO: Use true UPDATE instead of DELETE/ADD sequence
    case id :: Nil JsonPost json -> _ => {
      var actor = Helpers.tryo({ActorController.delete(id.toLong).get})
      if (actor.isDefined) {
        actor = Actor(
          mergeJson(actor.get, json)).map(ActorController.add(_).get
        )
      }
      actor.getOrElse(null) : JValue
    }
  })

  // Serve /api/endorsement and friends
  //Testing route: http://localhost:8080/api/endorsement/XXX
  serve( "api" / "endorsement" prefix {

    // /api/endorsement returns all the endorsements
    case Nil JsonGet _ => EndorsementController.allEndorsements: JValue

    // /api/endorsement/count gets the endorsement count
    case "count" :: Nil JsonGet _ => JInt(EndorsementController.allEndorsements.length)

    // /api/endorsement/endorsement_id gets the specified endorsement (or a 404)
    case id :: Nil JsonGet _ => Helpers.tryo({
      EndorsementController.find(id.toLong)
    }).getOrElse(Empty).map(a => a: JValue)

    // DELETE the endorsement in question
    case id :: Nil JsonDelete _ =>  Helpers.tryo({
      EndorsementController.delete(id.toLong)
    }).getOrElse(Empty).map(a => a: JValue)


    // PUT adds the endorsement if the JSON is parsable
    case Nil JsonPut Endorsement(endorsement) -> _ => EndorsementController.add(endorsement).get: JValue

    // POST if we find the endorsement, merge the fields from the
    // the POST body and update the endorsement
    //TODO: Use true UPDATE instead of DELETE/ADD sequence
    case id :: Nil JsonPost json -> _ => {
      var endorsement = Helpers.tryo({EndorsementController.delete(id.toLong).get})
      if (endorsement.isDefined) {
        endorsement = Endorsement(
          mergeJson(endorsement.get, json)).map(EndorsementController.add(_).get)
      }
      endorsement.getOrElse(null)  : JValue
    }
  })
}