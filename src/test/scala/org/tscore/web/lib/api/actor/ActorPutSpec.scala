package org.tscore.web.lib.api.actor

import org.tscore.web.lib.api.ActorSpec
import org.tscore.trust.model.Actor
import net.liftweb.http.js.JsExp

class ActorPutSpec extends ActorSpec {

  private val numActors = 99

  // some actors to test with
  val actorId = 99
  var actorWithoutId = Actor(null, "Test", "This is a test.", 99.0)
  var actorWithExplicitId = Actor(actorId, "Test", "This is a test.", -10.1)
  var actorWithoutDescription = Actor(null, "Test", null, 0)
  var actorWithoutScore = Actor(null, "Test", "This is a test.", null)
  var actorWithoutName = Actor(null, null, null, null)

  // empty repository
  repository = Nil

  "PUT /api/actor" should {

    "add new actor to an empty repository" withReqFor(request) withPut (actorWithoutId) in {
      req =>
        // initialize service with empty list
        repository.size mustEqual 0

        // send request and verify
        assertEqualsEntity(serve(req), actorWithoutId)

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual actorWithoutId
    }

    "add new actor to a repository that already contains some entities" withReqFor(request) withPut (actorWithoutId) in {
      req =>
        // initialize service with empty list
        for (i <- 1 until numActors+1) {
          repository ::= Actor(i, "Actor_"+i, "This is actor #"+i, i)
        }
        repository.size mustEqual numActors

        // send request and verify
        assertEqualsEntity(serve(req), actorWithoutId)

        // verify repository after the request has been served
        repository.size mustEqual numActors + 1
        repository.head mustEqual actorWithoutId
    }

    "add new actor without description" withReqFor(request) withPut (actorWithoutDescription) in {
      req =>
        // initialize service with empty list
        repository.size mustEqual 0

        // send request and verify
        assertEqualsEntity(serve(req), actorWithoutDescription)

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual actorWithoutDescription
    }

    "add new actor without score" withReqFor(request) withPut (actorWithoutScore) in {
      req =>
        // initialize service with empty list
        repository.size mustEqual 0

        // send request and verify
        assertEqualsEntity(serve(req), actorWithoutScore)

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual actorWithoutScore
    }

    "ignore explicitly set id in json payload" withReqFor(request) withPut (actorWithExplicitId) in {
      req =>
        // initialize service with empty list
        repository.size mustEqual 0

        // send request and verify
        assertNotEqualsEntity(serve(req), actorWithExplicitId)

        // verify repository after the request has been served
        repository.size mustEqual 1

        // actor in repository should have different Id
        repository.head.id mustNotEqual actorWithExplicitId.id

        // otherwise they should be the same
        actorWithExplicitId.id = repository.head.id
        repository.head mustEqual actorWithExplicitId
    }

    "return nothing if id set in path" withReqFor(endpoint + "/" + actorId) withPut (actorWithoutId) in {
      req =>
        // initialize service with empty list
        repository.size mustEqual 0

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 0
    }

    "return nothing for actor without name" withReqFor(request) withPut (actorWithoutName) in {
      req =>
        // initialize service with empty list
        repository.size mustEqual 0

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 0
    }
  }

  /**
   * Override parent method in order to set the id of a actor to the maxId inside the repository.
   * This way we can assert equality on newly inserted actors.
   *
   * @param jsExp   JsExp value to compare.
   * @param entity  Resource entity to compare against.
   * @return        Specs2 result.
   */

  override protected def assertEqualsEntity(jsExp: JsExp, entity: Actor): org.specs2.execute.Result = {
    if (entity.id == null) {
      entity.id = service.maxId
    }
    super.assertEqualsEntity(jsExp, entity)
  }
}
