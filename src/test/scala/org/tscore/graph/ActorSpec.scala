package org.tscore.graph

import org.tscore.graph.repository.{SubjectRepository, ActorRepository}
import org.tscore.graph.model.Actor
import scala.collection.JavaConversions._

class ActorSpec extends AbstractRepositorySpec {
  def repositories = Array(classOf[ActorRepository], classOf[SubjectRepository])

  test("save and find single actor") {
    // get the actor repository and make sure it's empty
    val repository = getRepository(classOf[ActorRepository])
    assert(repository.count() === 0, "repository not empty")

    // create a new actor
    val actor = new Actor()

    // save that actor
    val stored = repository.save(actor)

    // verify save
    assert(stored.id > 0, "invalid id for stored actor")
    assert(repository.count() === 1, "repository does not contain exactly 1 item")

    // find all
    for (fetched <- repository.findAll()) {
      assert(fetched === stored, "unexpected actor fetched")
    }

    // find by id
    assert(repository.findOne(stored.id) === stored, "unexpected actor fetched")

    // make sure that actor is just another subject
    val subject = getRepository(classOf[SubjectRepository]).findOne(stored.id)
    assert(subject != null, "no subject with id " + stored.id + " found")
  }
}
