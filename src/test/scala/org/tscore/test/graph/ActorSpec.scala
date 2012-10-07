package org.tscore.test.graph

import org.tscore.graph.repository.{SubjectRepository, ActorRepository}
import org.tscore.graph.model.Actor
import org.tscore.graph.util.Conversions._

class ActorSpec extends BaseRepositorySpec[Actor] {
  def getRepository : ActorRepository = ctx.getBean(classOf[ActorRepository])

  test("save and find single actor") {
    val repository = getRepository
    assert(repository.count() === 0, "repository not empty")

    val actor = new Actor
    actor.name = "foo"
    val stored = repository.save(actor)

    assert(stored.id > 0, "invalid id for stored actor")
    assert(repository.count() === 1, "repository does not contain exactly 1 item")

    for (fetched <- repository.findAll()) {
      assert(fetched === stored, "unexpected actor fetched")
    }

    val subjectRepo = ctx.getBean(classOf[SubjectRepository])
    val subject = subjectRepo.findOne(stored.id)
    assert(subject != null, "no subject with id " + stored.id + " found")
  }
}
