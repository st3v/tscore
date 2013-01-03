package org.tscore.web.lib.api.subject

import org.tscore.web.lib.api.SubjectSpec
import org.tscore.trust.model.Subject

class SubjectDeleteSpec extends SubjectSpec {
  // empty repository
  repository = Nil

  var numSubjects = 99
  var subjectId = 17
  var subject = Subject(subjectId, "Foo", "Bar")
  var endpointWithId = endpoint + "/" + subjectId

  "DELETE /api/subject" should {

    "delete and return subject for repository that stores exactly one subject" withReqFor(endpointWithId) withDelete () in {
      req =>
        repository ::= subject
        repository.size mustEqual 1

        assertEqualsEntity(serve(req), subject)

        repository.size mustEqual 0
    }

    "delete and return subject for repository that stores multiple subjects" withReqFor(endpointWithId) withDelete () in {
      req =>
        repository = Nil
        for (i <- 1 until numSubjects+1) {
          if (i != subjectId)
            repository ::= Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        repository ::= subject
        repository.size mustEqual numSubjects

        // send request and verify
        assertEqualsEntity(serve(req), subject)

        // verify repository after the request has been served
        repository.size mustEqual numSubjects-1
    }

    "return nothing for a non-existing id" withReqFor(endpointWithId) withDelete () in {
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

    "return nothing for an empty repository" withReqFor(endpointWithId) withDelete () in {
      req =>
        // initialize service with empty list
        repository.size mustEqual 0

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 0
    }

    "return nothing for a missing id" withReqFor(endpoint) withDelete () in {
      req =>
        repository ::= subject
        repository.size mustEqual 1

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 1
    }

    "return nothing for invalid id" withReqFor(endpoint + "/foo") withDelete () in {
      req =>
        repository ::= subject
        repository.size mustEqual 1

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 1
    }

    "return nothing for malformed path" withReqFor(endpoint + "/foo/" + subjectId) withDelete () in {
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
