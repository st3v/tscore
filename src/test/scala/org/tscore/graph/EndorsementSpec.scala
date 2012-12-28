package org.tscore.graph

import org.tscore.graph.model.{Endorsement, Subject, Actor}
import org.tscore.graph.repository.{EndorsementRepository, SubjectRepository, ActorRepository}
import org.springframework.transaction.annotation.Transactional
import scala.collection.JavaConversions._
import org.tscore.graph.ScoreMock._

@Transactional
class EndorsementSpec extends AbstractRepositorySpec {
  def repositories = Array(classOf[ActorRepository],
                           classOf[SubjectRepository],
                           classOf[EndorsementRepository])

  test("simple endorsement via direct save") {
    val actor = Actor("foo")
    val subject = Subject()
    val endorsement = Endorsement(actor, subject, 1.0)

    var savedActor = getRepository(classOf[ActorRepository]).save(actor)
    assert(savedActor != null)
    assert(savedActor.id > 0)

    val savedSubject = getRepository(classOf[SubjectRepository]).save(subject)
    assert(savedSubject != null)
    assert(savedSubject.id > 0)

    assert(actor.endorsed.size === 0)
    assert(actor.givenEndorsements.size === 0)

    val savedEndorsement = getRepository(classOf[EndorsementRepository]).save(endorsement)
    assert(savedEndorsement != null)
    assert(savedEndorsement.actor.id === actor.id)
    assert(savedEndorsement.subject.id === subject.id)

    savedActor = getRepository(classOf[ActorRepository]).findOne(actor.id)

    assert(savedActor.endorsed.size === 1)
    assert(savedActor.endorsed.iterator.next === subject)

    assert(savedActor.givenEndorsements.size === 1)
    assert(savedActor.givenEndorsements.iterator.next === endorsement)

  }

  test("simple endorsement via actor.endorse") {
    val subject = Subject()
    getRepository(classOf[SubjectRepository]).save(subject)
    assert(subject.endorsers.size === 0)
    assert(subject.receivedEndorsements.size === 0)

    val actor = Actor("foo")
    assert(actor.endorsers.size === 0)
    assert(actor.receivedEndorsements.size === 0)
    assert(actor.endorsed.size === 0)
    assert(actor.givenEndorsements.size === 0)

    actor.endorse(subject, 2.2)
    getRepository(classOf[ActorRepository]).save(actor)

    assert(actor.id > 0)
    assert(actor.endorsed.size === 1)
    assert(actor.givenEndorsements.size === 1)
    assert(actor.endorsed.iterator.next().id === subject.id)
    assert(actor.givenEndorsements.size === 1)
    assert(actor.givenEndorsements.iterator.next().score === 2.2)
    assert(actor.receivedEndorsements.size === 0)
    assert(actor.endorsers.size === 0)

    assert(subject.id > 0)
    assert(subject.endorsers.size === 1)
    assert(subject.endorsers.iterator.next() === actor)
    assert(subject.receivedEndorsements.iterator.next().score === 2.2)

    assert(getRepository(classOf[EndorsementRepository]).count() === 1)
    for (endorsement <- getRepository(classOf[EndorsementRepository]).findAll()) {
      assert(endorsement.actor.id === actor.id)
      assert(endorsement.subject.id === subject.id)
      assert(endorsement.score === 2.2)
    }
  }

}
