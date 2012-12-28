package org.tscore.graph

import org.tscore.graph.repository.SubjectRepository
import org.tscore.graph.model.Subject
import scala.collection.JavaConversions._
import collection.mutable

class SubjectSpec extends AbstractRepositorySpec {
  def repositories = Array(classOf[org.tscore.graph.repository.SubjectRepository])

  test("save and find single subject") {
    val repository = getRepository(classOf[SubjectRepository])
    assert(repository.count() === 0, "repository not empty")

    val subject = new Subject
    val stored = repository.save(subject)

    assert(stored.id > 0, "invalid id for stored subject")
    assert(repository.count() === 1, "repository does not contain exactly 1 item")

    for (fetchedSubject <- repository.findAll()) {
      assert(fetchedSubject.id === stored.id, "unexpected id for fetched subject")
    }
  }

  test("save and find multiple subjects") {
    val repository = getRepository(classOf[SubjectRepository])
    assert(repository.count() === 0, "repository not empty")

    var subjects = new mutable.ArrayBuffer[Subject](10)
    for (i <- 0 until 9) {
      subjects += repository.save(new Subject)
      assert(subjects(i).id > 0, "invalid id for stored subject")
      assert(repository.count() === subjects.length, "repository does not contain exactly " + subjects.length + " item(s)")
    }

    var i = 0
    for (fetchedSubject <- repository.findAll()) {
      assert(fetchedSubject.id === subjects(i).id, "unexpected id for fetched subject with index " + i)
      i = i + 1
    }
  }
}
