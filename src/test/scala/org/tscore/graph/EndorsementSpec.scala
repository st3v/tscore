package org.tscore.graph

import org.tscore.graph.model.{Endorsement, Subject, Actor}
import org.tscore.graph.repository.{EndorsementRepository, SubjectRepository, ActorRepository}
import org.springframework.transaction.annotation.Transactional

@Transactional
class EndorsementSpec extends AbstractRepositorySpec {
  def repositories = Array(classOf[ActorRepository],
                           classOf[SubjectRepository],
                           classOf[EndorsementRepository])

  test("simple endorsement via direct save") {
    val actor = new Actor()
    val subject = new Subject()
    val endorsement = Endorsement(actor, subject)

    var savedActor = getRepository(classOf[ActorRepository]).save(actor)
    assert(savedActor != null)
    assert(savedActor.id > 0)

    val savedSubject = getRepository(classOf[SubjectRepository]).save(subject)
    assert(savedSubject != null)
    assert(savedSubject.id > 0)

    assert(actor.endorsed.size == 0)
    assert(actor.givenEndorsements.size == 0)

    val savedEndorsement = getRepository(classOf[EndorsementRepository]).save(endorsement)
    assert(savedEndorsement != null)
    assert(savedEndorsement.actor.id == actor.id)
    assert(savedEndorsement.subject.id == subject.id)

    savedActor = getRepository(classOf[ActorRepository]).findOne(actor.id)

    assert(savedActor.endorsed.size == 1)
    assert(savedActor.endorsed.iterator.next == subject)

    assert(savedActor.givenEndorsements.size == 1)
    assert(savedActor.givenEndorsements.iterator.next == endorsement)

  }

  test("simple endorsement via actor.endorse") {
    val subject = new Subject()
    getRepository(classOf[SubjectRepository]).save(subject)
    assert(subject.endorsers.size == 0)
    assert(subject.receivedEndorsements.size == 0)

    val actor = new Actor()
    assert(actor.endorsers.size == 0)
    assert(actor.receivedEndorsements.size == 0)
    assert(actor.endorsed.size == 0)
    assert(actor.givenEndorsements.size == 0)

    actor.endorse(subject)
    getRepository(classOf[ActorRepository]).save(actor)

    assert(actor.id > 0)
    assert(actor.endorsed.size == 1)
    assert(actor.givenEndorsements.size == 1)
    assert(actor.endorsed.iterator.next().id == subject.id)
    assert(actor.givenEndorsements.size == 1)
    assert(actor.givenEndorsements.iterator.next().subject == subject)
    assert(actor.receivedEndorsements.size == 0)
    assert(actor.endorsers.size == 0)

    assert(subject.id > 0)
    assert(subject.endorsers.size === 1)
    assert(subject.endorsers.iterator.next() === actor)
    assert(subject.receivedEndorsements.iterator.next().actor == actor)

    val endorsements = getRepository(classOf[EndorsementRepository]).findAll.as(classOf[java.util.List[Endorsement]])
    assert(endorsements.size == 1)
    val iterator = endorsements.iterator
    while (iterator.hasNext) {
      val endorsement = iterator.next
      assert(endorsement.actor.id === actor.id)
      assert(endorsement.subject.id === subject.id)
    }
  }

}
