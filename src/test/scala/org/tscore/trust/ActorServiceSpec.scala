package org.tscore.trust

import org.tscore.trust.service.impl.ActorService
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FunSuite}
import org.springframework.context.support.GenericXmlApplicationContext
import org.tscore.trust.repository.ActorRepository
import org.tscore.trust.model.score.TrustScore
import org.tscore.TestSuite

class ActorServiceSpec extends TestSuite with BeforeAndAfterAll with BeforeAndAfter {
  var service: ActorService = null
  var ctx: GenericXmlApplicationContext = null
  var actorRepository: ActorRepository = null

  override def beforeAll() {
    ctx = new GenericXmlApplicationContext("classpath*:/META-INF/spring/module-context-trust-test.xml")
    service = ctx.getBean(classOf[ActorService])
    actorRepository = ctx.getBean(classOf[ActorRepository])
  }

  before {
    actorRepository.deleteAll()
  }

  override def afterAll() {
    ctx.close()
  }

  test("create actor without score") {
    val actor = service.create("Stev")
    assert(actor.score == TrustScore.zero)
  }


  test("create, save, find, and update single actor") {
    // create new actor
    val actor = service.create("Stev", 2.0)

    // store the new actor
    service.addActor(actor)

    // fetch the stored actor and validate
    var found = service.findActorByName("Stev")
    assert(found.name === "Stev")
    assert(found.score === 2.0)

    // update actor score
    actor.score = -1
    assert(actor.score != found.score)

    // safe changes to actor
    service.addActor(actor)

    // re-fetch actor and validate changes
    found = service.findActorByName("Stev")
    assert(actor.score === found.score)
    assert(found.score == -1)
    assert(found.score == -1.0)
    assert(found.score < 0)
    assert(found.score <= -1)
    assert(found.score <= 0)
    assert(found.score > -2)
    assert(found.score >= -1)
    assert(found.score >= -2)
    assert(found.score == new TrustScore(-1))
    assert(found.score == new TrustScore(-1.0))
    assert(found.score < new TrustScore(0))
    assert(found.score <= new TrustScore(-1))
    assert(found.score <= new TrustScore(0))
    assert(found.score > new TrustScore(-2))
    assert(found.score >= new TrustScore(-1))
    assert(found.score >= new TrustScore(-2))

    val actors = service.getAllActors
    assert(actors.size == 1)
  }
}



