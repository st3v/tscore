package org.tscore.web.lib.api.actor

import org.tscore.web.lib.api.ActorSpec
import org.tscore.trust.model.Actor

class ActorDeleteSpec extends ActorSpec {
  // empty repository
  repository = Nil

  var numActors = 99
  var actorId = 17
  var actor = Actor(actorId, "Foo", "Bar", 99.9)
  var endpointWithId = endpoint + "/" + actorId

  "DELETE /api/actor" should {

    "delete and return actor for repository that stores exactly one actor" withReqFor(endpointWithId) withDelete () in {
      req =>
        repository ::= actor
        repository.size mustEqual 1

        assertEqualsEntity(serve(req), actor)

        repository.size mustEqual 0
    }

    "delete and return actor for repository that stores multiple actors" withReqFor(endpointWithId) withDelete () in {
      req =>
        repository = Nil
        for (i <- 1 until numActors+1) {
          if (i != actorId)
            repository ::= Actor(i, "Actor_"+i, "This is actor #"+i, i)
        }
        repository ::= actor
        repository.size mustEqual numActors

        // send request and verify
        assertEqualsEntity(serve(req), actor)

        // verify repository after the request has been served
        repository.size mustEqual numActors-1
    }

    "return nothing for a non-existing id" withReqFor(endpointWithId) withDelete () in {
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

    "return nothing for an empty repository" withReqFor(endpointWithId) withDelete () in {
      req =>
        // initialize service with empty list
        repository.size mustEqual 0

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 0
    }

    "return nothing for a missing id" withReqFor(endpoint) withDelete () in {
      req =>
        repository ::= actor
        repository.size mustEqual 1

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 1
    }

    "return nothing for invalid id" withReqFor(endpoint + "/foo") withDelete () in {
      req =>
        repository ::= actor
        repository.size mustEqual 1

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 1
    }

    "return nothing for malformed path" withReqFor(endpoint + "/foo/" + actorId) withDelete () in {
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
