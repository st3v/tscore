package org.tscore.test.trust

import org.tscore.trust.TrustService
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FunSuite}
import org.springframework.context.support.GenericXmlApplicationContext
import org.tscore.graph.repository.ActorRepository
import org.tscore.trust.score.TrustScore._
import org.tscore.trust.score.{NumericScore, TrustScore}

class TrustServiceSpec extends FunSuite with BeforeAndAfterAll with BeforeAndAfter {
  var service: TrustService = null
  var ctx: GenericXmlApplicationContext = null
  var actorRepository: ActorRepository = null

  override def beforeAll() {
    ctx = new GenericXmlApplicationContext("classpath*:/META-INF/spring/module-context-test.xml")
    service = ctx.getBean(classOf[TrustService])
    actorRepository = ctx.getBean(classOf[ActorRepository])
  }

  before {
    actorRepository.deleteAll()
  }

  override def afterAll() {
    ctx.close()
  }

  test("invalid cast") {
    // create an Actor
    val actor = org.tscore.graph.model.Actor("stev")
    actorRepository.save(actor)

    // try to fetch as a TrustActor
    val found = service.findActor("stev")
    assert(found === null)
  }

  test("create, save, find, and update single actor") {
    // create new actor
    val actor = service.createActor("Stev", 2.0)

    // store the new actor
    service.saveActor(actor)

    // fetch the stored actor and validate
    var found = service.findActor("Stev")
    assert(found.name === "Stev")
    assert(found.score === 2.0)

    // update actor score
    actor.score = -1
    assert(actor.score != found.score)

    // safe changes to actor
    service.saveActor(actor)

    // re-fetch actor and validate changes
    found = service.findActor("Stev")
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
  }
}



