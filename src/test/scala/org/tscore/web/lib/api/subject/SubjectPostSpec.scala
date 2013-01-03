package org.tscore.web.lib.api.subject

import org.tscore.web.lib.api.SubjectSpec
import org.tscore.trust.model.Subject
import net.liftweb.json.JsonDSL._
import net.liftweb.json.JsonAST.{JNull, JNothing}

class SubjectPostSpec extends SubjectSpec {
  val subjectId = 17
  val numSubjects = 99

  val endpointWithId = endpoint + "/" + subjectId

  def subject = Subject(17, "Original Name", "Original Description")

  val newName = "New Name"
  val newDescription = "New Description"

  val subjectNameUpdate = ("name" -> newName)
  val subjectDescriptionUpdate = ("description" -> newDescription)
  val subjectFullUpdate = subjectNameUpdate ~ subjectDescriptionUpdate
  val subjectIdUpdate = ("id" -> (subjectId + numSubjects))
  val subjectNothingUpdate = JNothing
  val subjectNullUpdate = JNull

  // empty repository
  repository = Nil

  "POST /api/subject/<id>" should {

    "change name and return updated subject for repository that stores exactly one subject" withReqFor(endpointWithId) withPost (subjectNameUpdate) in {
      req =>
        repository.size mustEqual 0
        repository ::= subject
        repository.size mustEqual 1
        repository.head mustEqual subject

        val newSubject = subject
        newSubject.name = newName
        repository.head mustNotEqual newSubject

        // send request and verify
        assertEqualsEntity(serve(req), newSubject)

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual newSubject
    }

    "change name and return updated subject for repository that stores multiple subjects" withReqFor(endpointWithId) withPost (subjectNameUpdate) in {
      req =>
        for (i <- 1 until numSubjects+1) {
          if (i != subjectId)
            repository ::= Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        repository ::= subject
        repository.size mustEqual numSubjects
        service.repository(subjectId) mustEqual subject

        val newSubject = subject
        newSubject.name = newName
        service.repository(subjectId) mustNotEqual newSubject

        // send request and verify
        assertEqualsEntity(serve(req), newSubject)

        // verify repository after the request has been served
        repository.size mustEqual numSubjects
        service.repository(subjectId) mustEqual newSubject

        // everything else should not have been affected by this update
        var same = true
        for (i <- 1 until numSubjects+1) {
          if (i != subjectId)
            same &&= service.repository(i) == Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        same mustEqual true
    }

    "change description and return updated subject for repository that stores exactly one subject" withReqFor(endpointWithId) withPost (subjectDescriptionUpdate) in {
      req =>
        repository.size mustEqual 0
        repository ::= subject
        repository.size mustEqual 1
        repository.head mustEqual subject

        val newSubject = subject
        newSubject.description = newDescription
        repository.head mustNotEqual newSubject

        // send request and verify
        assertEqualsEntity(serve(req), newSubject)

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual newSubject
    }

    "change name and return updated subject for repository that stores multiple subjects" withReqFor(endpointWithId) withPost (subjectDescriptionUpdate) in {
      req =>
        for (i <- 1 until numSubjects+1) {
          if (i != subjectId)
            repository ::= Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        repository ::= subject
        repository.size mustEqual numSubjects
        service.repository(subjectId) mustEqual subject

        val newSubject = subject
        newSubject.description = newDescription
        service.repository(subjectId) mustNotEqual newSubject

        // send request and verify
        assertEqualsEntity(serve(req), newSubject)

        // verify repository after the request has been served
        repository.size mustEqual numSubjects
        service.repository(subjectId) mustEqual newSubject

        // everything else should not have been affected by this update
        var same = true
        for (i <- 1 until numSubjects+1) {
          if (i != subjectId)
            same &&= service.repository(i) == Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        same mustEqual true
    }

    "change all subject fields and return updated subject for repository that stores exactly one subject" withReqFor(endpointWithId) withPost (subjectFullUpdate) in {
      req =>
        repository.size mustEqual 0
        repository ::= subject
        repository.size mustEqual 1
        repository.head mustEqual subject

        val newSubject = subject
        newSubject.name = newName
        newSubject.description = newDescription
        repository.head mustNotEqual newSubject

        // send request and verify
        assertEqualsEntity(serve(req), newSubject)

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual newSubject
    }

    "change all subject fields and return updated subject for repository that stores multiple subjects" withReqFor(endpointWithId) withPost (subjectFullUpdate) in {
      req =>
        for (i <- 1 until numSubjects+1) {
          if (i != subjectId)
            repository ::= Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        repository ::= subject
        repository.size mustEqual numSubjects
        service.repository(subjectId) mustEqual subject

        val newSubject = subject
        newSubject.name = newName
        newSubject.description = newDescription
        service.repository(subjectId) mustNotEqual newSubject

        // send request and verify
        assertEqualsEntity(serve(req), newSubject)

        // verify repository after the request has been served
        repository.size mustEqual numSubjects
        service.repository(subjectId) mustEqual newSubject

        // everything else should not have been affected by this update
        var same = true
        for (i <- 1 until numSubjects+1) {
          if (i != subjectId)
            same &&= service.repository(i) == Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        same mustEqual true
    }

    "ignore id field specified in the payload" withReqFor(endpointWithId) withPost (subjectIdUpdate) in {
      req =>
        repository.size mustEqual 0
        repository ::= subject
        repository.size mustEqual 1
        repository.head mustEqual subject

        // send request and verify
        assertEqualsEntity(serve(req), subject)

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual subject
    }

    "return nothing and leave the stored subject as is for empty payload" withReqFor(endpointWithId) withPost (subjectNullUpdate) in {
      req =>
        repository.size mustEqual 0
        repository ::= subject
        repository.size mustEqual 1
        repository.head mustEqual subject

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual subject
    }

    "return nothing for a non-existing id" withReqFor(endpointWithId) withPost (subjectFullUpdate) in {
      req =>
        repository = Nil
        for (i <- 1 until numSubjects+1) {
          if (i != subjectId)
            repository ::= Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        repository.size mustEqual numSubjects-1

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual numSubjects-1
    }

    "return nothing for an empty repository" withReqFor(endpointWithId) withPost (subjectFullUpdate) in {
      req =>
        // initialize service with empty list
        repository.size mustEqual 0

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 0
    }

    "return nothing for a missing id" withReqFor(endpoint) withPost (subjectFullUpdate) in {
      req =>
        repository ::= subject
        repository.size mustEqual 1

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 1
    }

    "return nothing for invalid id" withReqFor(endpoint + "/foo") withPost (subjectFullUpdate) in {
      req =>
        repository ::= subject
        repository.size mustEqual 1

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 1
    }

    "return nothing for malformed path" withReqFor(endpoint + "/foo/" + subjectId) withPost (subjectFullUpdate) in {
      req =>
        repository ::= subject
        repository.size mustEqual 1

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 1
    }

    "return nothing for path with extra slash" withReqFor(endpointWithId + "/") withDelete () in {
      req =>
        repository ::= subject
        repository.size mustEqual 1

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 1
    }

  }
}
