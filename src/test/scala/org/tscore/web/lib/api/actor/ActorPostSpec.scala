package org.tscore.web.lib.api.actor

import org.tscore.web.lib.api.ActorSpec
import org.tscore.trust.model.Actor
import net.liftweb.json.JsonDSL._
import net.liftweb.json.JsonAST.{JNull, JNothing}

class ActorPostSpec extends ActorSpec {
  val actorId = 17
  val numActors = 99

  val endpointWithId = endpoint + "/" + actorId

  def actor = Actor(17, "Original Name", "Original Description", 99.9)

  val newName = "New Name"
  val newDescription = "New Description"
  val newScore = -1.1

  val actorNameUpdate = ("name" -> newName)
  val actorDescriptionUpdate = ("description" -> newDescription)
  val actorScoreUpdate = ("score" -> newScore)
  val actorFullUpdate = actorNameUpdate ~ actorDescriptionUpdate ~ actorScoreUpdate
  val actorIdUpdate = ("id" -> (actorId + numActors))
  val actorNothingUpdate = JNothing
  val actorNullUpdate = JNull

  // empty repository
  repository = Nil

  "POST /api/actor/<id>" should {

    "change name and return updated actor for repository that stores exactly one actor" withReqFor(endpointWithId) withPost (actorNameUpdate) in {
      req =>
        repository.size mustEqual 0
        repository ::= actor
        repository.size mustEqual 1
        repository.head mustEqual actor

        val newActor = actor
        newActor.name = newName
        repository.head mustNotEqual newActor

        // send request and verify
        assertEqualsEntity(serve(req), newActor)

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual newActor
    }

    "change name and return updated actor for repository that stores multiple actors" withReqFor(endpointWithId) withPost (actorNameUpdate) in {
      req =>
        for (i <- 1 until numActors+1) {
          if (i != actorId)
            repository ::= Actor(i, "Actor_"+i, "This is actor #"+i, i)
        }
        repository ::= actor
        repository.size mustEqual numActors
        service.repository(actorId) mustEqual actor

        val newActor = actor
        newActor.name = newName
        service.repository(actorId) mustNotEqual newActor

        // send request and verify
        assertEqualsEntity(serve(req), newActor)

        // verify repository after the request has been served
        repository.size mustEqual numActors
        service.repository(actorId) mustEqual newActor

        // everything else should not have been affected by this update
        var same = true
        for (i <- 1 until numActors+1) {
          if (i != actorId)
            same &&= service.repository(i) == Actor(i, "Actor_"+i, "This is actor #"+i, i)
        }
        same mustEqual true
    }

    "change description and return updated actor for repository that stores exactly one actor" withReqFor(endpointWithId) withPost (actorDescriptionUpdate) in {
      req =>
        repository.size mustEqual 0
        repository ::= actor
        repository.size mustEqual 1
        repository.head mustEqual actor

        val newActor = actor
        newActor.description = newDescription
        repository.head mustNotEqual newActor

        // send request and verify
        assertEqualsEntity(serve(req), newActor)

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual newActor
    }

    "change description and return updated actor for repository that stores multiple actors" withReqFor(endpointWithId) withPost (actorDescriptionUpdate) in {
      req =>
        for (i <- 1 until numActors+1) {
          if (i != actorId)
            repository ::= Actor(i, "Actor_"+i, "This is actor #"+i, i)
        }
        repository ::= actor
        repository.size mustEqual numActors
        service.repository(actorId) mustEqual actor

        val newActor = actor
        newActor.description = newDescription
        service.repository(actorId) mustNotEqual newActor

        // send request and verify
        assertEqualsEntity(serve(req), newActor)

        // verify repository after the request has been served
        repository.size mustEqual numActors
        service.repository(actorId) mustEqual newActor

        // everything else should not have been affected by this update
        var same = true
        for (i <- 1 until numActors+1) {
          if (i != actorId)
            same &&= service.repository(i) == Actor(i, "Actor_"+i, "This is actor #"+i, i)
        }
        same mustEqual true
    }

    "change score and return updated actor for repository that stores exactly one actor" withReqFor(endpointWithId) withPost (actorScoreUpdate) in {
      req =>
        repository.size mustEqual 0
        repository ::= actor
        repository.size mustEqual 1
        repository.head mustEqual actor

        val newActor = actor
        newActor.score = newScore
        repository.head mustNotEqual newActor

        // send request and verify
        assertEqualsEntity(serve(req), newActor)

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual newActor
    }

    "change score and return updated actor for repository that stores multiple actors" withReqFor(endpointWithId) withPost (actorScoreUpdate) in {
      req =>
        for (i <- 1 until numActors+1) {
          if (i != actorId)
            repository ::= Actor(i, "Actor_"+i, "This is actor #"+i, i)
        }
        repository ::= actor
        repository.size mustEqual numActors
        service.repository(actorId) mustEqual actor

        val newActor = actor
        newActor.score = newScore
        service.repository(actorId) mustNotEqual newActor

        // send request and verify
        assertEqualsEntity(serve(req), newActor)

        // verify repository after the request has been served
        repository.size mustEqual numActors
        service.repository(actorId) mustEqual newActor

        // everything else should not have been affected by this update
        var same = true
        for (i <- 1 until numActors+1) {
          if (i != actorId)
            same &&= service.repository(i) == Actor(i, "Actor_"+i, "This is actor #"+i, i)
        }
        same mustEqual true
    }

    "change all actor fields and return updated actor for repository that stores exactly one actor" withReqFor(endpointWithId) withPost (actorFullUpdate) in {
      req =>
        repository.size mustEqual 0
        repository ::= actor
        repository.size mustEqual 1
        repository.head mustEqual actor

        val newActor = actor
        newActor.name = newName
        newActor.description = newDescription
        newActor.score = newScore
        repository.head mustNotEqual newActor

        // send request and verify
        assertEqualsEntity(serve(req), newActor)

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual newActor
    }

    "change all actor fields and return updated actor for repository that stores multiple actors" withReqFor(endpointWithId) withPost (actorFullUpdate) in {
      req =>
        for (i <- 1 until numActors+1) {
          if (i != actorId)
            repository ::= Actor(i, "Actor_"+i, "This is actor #"+i, i)
        }
        repository ::= actor
        repository.size mustEqual numActors
        service.repository(actorId) mustEqual actor

        val newActor = actor
        newActor.name = newName
        newActor.description = newDescription
        newActor.score = newScore
        service.repository(actorId) mustNotEqual newActor

        // send request and verify
        assertEqualsEntity(serve(req), newActor)

        // verify repository after the request has been served
        repository.size mustEqual numActors
        service.repository(actorId) mustEqual newActor

        // everything else should not have been affected by this update
        var same = true
        for (i <- 1 until numActors+1) {
          if (i != actorId)
            same &&= service.repository(i) == Actor(i, "Actor_"+i, "This is actor #"+i, i)
        }
        same mustEqual true
    }

    "ignore id field specified in the payload" withReqFor(endpointWithId) withPost (actorIdUpdate) in {
      req =>
        repository.size mustEqual 0
        repository ::= actor
        repository.size mustEqual 1
        repository.head mustEqual actor

        // send request and verify
        assertEqualsEntity(serve(req), actor)

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual actor
    }

    "return nothing and leave the stored actor as is for empty payload" withReqFor(endpointWithId) withPost (actorNullUpdate) in {
      req =>
        repository.size mustEqual 0
        repository ::= actor
        repository.size mustEqual 1
        repository.head mustEqual actor

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual actor
    }

    "return nothing for a non-existing id" withReqFor(endpointWithId) withPost (actorFullUpdate) in {
      req =>
        repository = Nil
        for (i <- 1 until numActors+1) {
          if (i != actorId)
            repository ::= Actor(i, "Actor_"+i, "This is actor #"+i, i)
        }
        repository.size mustEqual numActors-1

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual numActors-1
    }

    "return nothing for an empty repository" withReqFor(endpointWithId) withPost (actorFullUpdate) in {
      req =>
        // initialize service with empty list
        repository.size mustEqual 0

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 0
    }

    "return nothing for a missing id" withReqFor(endpoint) withPost (actorFullUpdate) in {
      req =>
        repository ::= actor
        repository.size mustEqual 1

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 1
    }

    "return nothing for invalid id" withReqFor(endpoint + "/foo") withPost (actorFullUpdate) in {
      req =>
        repository ::= actor
        repository.size mustEqual 1

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 1
    }

    "return nothing for malformed path" withReqFor(endpoint + "/foo/" + actorId) withPost (actorFullUpdate) in {
      req =>
        repository ::= actor
        repository.size mustEqual 1

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 1
    }

    "return nothing for path with extra slash" withReqFor(endpointWithId + "/") withDelete () in {
      req =>
        repository ::= actor
        repository.size mustEqual 1

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 1
    }

  }
}
