package org.tscore.trust.service

import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.springframework.context.support.GenericXmlApplicationContext
import org.tscore.trust.service.repository.ActorRepository
import org.tscore.trust.model.score.ActorScore
import org.tscore.TestSuite

class ActorServiceSpec extends TestSuite with BeforeAndAfterAll with BeforeAndAfter {
  var service: ActorService = null
  var ctx: GenericXmlApplicationContext = null
  var repository: ActorRepository = null

  override def beforeAll() {
    ctx = new GenericXmlApplicationContext("classpath*:/META-INF/spring/module-context-test-trust.xml")
    service = ctx.getBean(classOf[ActorService])
    repository = ctx.getBean(classOf[ActorRepository])
  }

  before {
    repository.deleteAll()
  }

  override def afterAll() {
    ctx.close()
  }

  test("create actor with name only") {
    val actor = service.create(None, "Stev", None, None)
    assert(actor.isDefined)
    assert(actor.get.id == null)
    assert(actor.get.name == "Stev")
    assert(actor.get.description == "")
    assert(actor.get.score == ActorScore.zero)
  }

  test("create and store actor with explicit id") {
    val requestedId = 9999

    var actor = service.create(Some(requestedId), "Stev", None, None)
    assert(actor.isDefined)
    assert(actor.get.id == requestedId)
    assert(actor.get.name == "Stev")
    assert(actor.get.description == "")
    assert(actor.get.score == ActorScore.zero)

    actor = service.add(actor.get)
    assert(actor.isDefined)
    val actualId =  actor.get.id
    assert(actualId != requestedId)
    assert(actor.get.name == "Stev")
    assert(actor.get.description == "")
    assert(actor.get.score == ActorScore.zero)

    actor = service.find(requestedId)
    assert(actor.isEmpty)

    actor = service.find(actualId)
    assert(actor.isDefined)
    assert(actor.get.id == actualId)
    assert(actor.get.name == "Stev")
    assert(actor.get.description == "")
    assert(actor.get.score == ActorScore.zero)
  }

  test("create, store, find, and update single actor") {
    // create new actor
    var actor = service.create(None, "Stev", Some("Description"), Some(2.0))
    assert(actor.isDefined)
    assert(actor.get.id == null)
    assert(actor.get.name == "Stev")
    assert(actor.get.description == "Description")
    assert(actor.get.score == 2.0)

    // store the new actor
    actor = service.add(actor.get)
    assert(actor.isDefined)
    assert(actor.get.id >= 0)
    assert(actor.get.name == "Stev")
    assert(actor.get.description == "Description")
    assert(actor.get.score == 2.0)

    val actorId = actor.get.id
    var found = service.find(actorId)
    assert(found.isDefined)
    assert(found.get.id == actorId)
    assert(found.get.name == "Stev")
    assert(found.get.score == 2.0)
    assert(found.get.description == "Description")

    // fetch the stored actor and validate
    found = service.find("Stev")
    assert(found.isDefined)
    assert(found.get.id == actorId)
    assert(found.get.name == "Stev")
    assert(found.get.score == 2.0)
    assert(found.get.description == "Description")

    // update actor score
    actor.get.score = -1
    assert(actor.isDefined)
    assert(actor.get.id == actorId)
    assert(actor.get.name == "Stev")
    assert(actor.get.description == "Description")
    assert(actor.get.score == -1)
    assert(actor.get.score != found.get.score)

    // safe changes to actor
    actor = service.add(actor.get)
    assert(actor.isDefined)
    assert(actor.get.id == actorId)
    assert(actor.get.name == "Stev")
    assert(actor.get.description == "Description")
    assert(actor.get.score == -1)

    // re-fetch actor and validate changes
    found = service.find(actorId)
    assert(found.isDefined)
    assert(found.get.id == actorId)
    assert(found.get.name == "Stev")
    assert(found.get.description == "Description")
    assert(found.get.score == actor.get.score)

    // re-fetch actor and validate changes
    found = service.find("Stev")
    assert(found.isDefined)
    assert(found.get.id == actorId)
    assert(found.get.name == "Stev")
    assert(found.get.description == "Description")
    assert(found.get.score == actor.get.score)

    // make sure scores work as expected
    assert(found.get.score == -1)
    assert(found.get.score == -1.0)
    assert(found.get.score < 0)
    assert(found.get.score <= -1)
    assert(found.get.score <= 0)
    assert(found.get.score > -2)
    assert(found.get.score >= -1)
    assert(found.get.score >= -2)
    assert(found.get.score == new ActorScore(-1))
    assert(found.get.score == new ActorScore(-1.0))
    assert(found.get.score < new ActorScore(0))
    assert(found.get.score <= new ActorScore(-1))
    assert(found.get.score <= new ActorScore(0))
    assert(found.get.score > new ActorScore(-2))
    assert(found.get.score >= new ActorScore(-1))
    assert(found.get.score >= new ActorScore(-2))

    // make sure there is a single actor inside the repository
    val actors = service.getAll
    assert(actors.size == 1)
    assert(actors.head.id == actorId)
  }
}



