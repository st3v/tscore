package org.tscore.web.lib.api.actor

import org.tscore.web.lib.api.ActorSpec
import org.tscore.trust.model.Actor


class ActorGetSpec extends ActorSpec {
  private val numActors = 99
  private val actorId = 17

  "GET /api/actor" should {

    "return empty list" withReqFor(request) in {
      req =>
        // initialize service with empty list
        repository = Nil
        repository.size mustEqual 0

        // serve the request and validate response
        assertEmptyEntityList(serve(req))
    }

    "return list with one actor" withReqFor(request) in {
      req =>
        repository = List(Actor(99, "Stev", "This is a test", 1.1))
        repository.size mustEqual 1

        // serve the request and validate response
        assertEqualsEntityList(serve(req), repository)
    }

    "return list with multiple actors" withReqFor(request) in {
      req =>
        repository = Nil
        for (i <- 1 until numActors+1) {
          repository ::= Actor(i, "Actor_"+i, "This is actor #"+i, i)
        }
        repository.size mustEqual numActors

        // serve the request and validate response
        assertEqualsEntityList(serve(req), repository)
    }

  }

  "GET /api/actor/count" should {

    "return 0 for repository that stores nothing" withReqFor(endpoint + "/count") in {
      req =>
        // empty repository
        repository = Nil

        // serve the request and validate response
        assertEqualsNumber(serve(req), 0)
    }

    "return 1 for repository that stores one actor" withReqFor(endpoint + "/count") in {
      req =>
        repository = Nil
        repository ::= Actor(actorId, "Foo", "This is a test.", 17.01)
        repository.size mustEqual 1

        // serve the request and validate response
        assertEqualsNumber(serve(req), 1)
    }

    "return correct count for repository that stores many actors" withReqFor(endpoint + "/count") in {
      req =>
        repository = Nil
        for (i <- 1 until numActors+1) {
          repository ::= Actor(i, "Actor_"+i, "This is actor #"+i, i)
        }
        repository.size mustEqual numActors

        // serve the request and validate response
        assertEqualsNumber(serve(req), numActors)
    }

    "return nothing for path ending with a slash" withReqFor(endpoint + "/count/") in {
      req =>
        assertEmptyResponse(serve(req))
    }

    "return nothing for malformed path" withReqFor(endpoint + "/count/oopps") in {
      req =>
        assertEmptyResponse(serve(req))
    }

  }

  "GET /api/actor/<id>" should {

    "return nothing from empty repository" withReqFor(endpoint + "/" + actorId) in {
      req =>
        // empty repository
        repository = Nil

        // serve the request and validate response
        assertEmptyResponse(serve(req))
    }

    "return nothing for non-existing id" withReqFor(endpoint + "/" + actorId) in {
      req =>
        repository = Nil
        for (i <- 1 until numActors+1) {
          if (i != actorId)
            repository ::= Actor(i, "Actor_"+i, "This is actor #"+i, i)
        }
        repository.size mustEqual numActors-1

        // serve the request and validate response
        assertEmptyResponse(serve(req))
    }

    "return single actor from repository that stores exactly one" withReqFor(endpoint + "/" + actorId) in {
      req =>
        repository = Nil
        repository ::= Actor(actorId, "Actor", "This is a test.")
        repository.size mustEqual 1

        // serve the request and validate response
        assertEqualsEntity(serve(req), service.repository(actorId))
    }

    "return single actor from repository that stores many" withReqFor(endpoint + "/" + actorId) in {
      req =>
        repository = Nil
        for (i <- 1 until numActors+1) {
          repository ::= Actor(i, "Actor_"+i, "This is actor #"+i, i)
        }
        repository.size mustEqual numActors

        // serve the request and validate response
        assertEqualsEntity(serve(req), service.repository(actorId))
    }

    "return nothing for invalid id" withReqFor(endpoint + "/not-an-id") in {
      req =>
        assertEmptyResponse(serve(req))
    }

    "return nothing for missing id" withReqFor(endpoint + "/") in {
      req =>
        assertEmptyResponse(serve(req))
    }

    "return nothing for malformed path" withReqFor(endpoint + "/foo/" + actorId) in {
      req =>
        assertEmptyResponse(serve(req))
    }

  }

}
