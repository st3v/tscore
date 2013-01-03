package org.tscore.web.lib.api.subject

import org.tscore.web.lib.api.SubjectSpec
import org.tscore.trust.model.Subject


class SubjectGetSpec extends SubjectSpec {
  private val numSubjects = 99
  private val subjectId = 17

  "GET /api/subject" should {

    "return empty list" withReqFor(request) in {
      req =>
        // initialize service with empty list
        repository = Nil
        repository.size mustEqual 0

        // serve the request and validate response
        assertEmptyEntityList(serve(req))
    }

    "return list with one subject" withReqFor(request) in {
      req =>
        // initialize service with exactly one subject
        repository = List(Subject(99, "Stev", "This is a test"))
        repository.size mustEqual 1

        // serve the request and validate response
        assertEqualsEntityList(serve(req), repository)
    }

    "return list with multiple subjects" withReqFor(request) in {
      req =>
        // initialize service with numSubjects-1 subjects
        repository = Nil
        for (i <- 1 until numSubjects+1) {
          repository ::= Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        repository.size mustEqual numSubjects

        // serve the request and validate response
        assertEqualsEntityList(serve(req), repository)
    }

  }

  "GET /api/subject/count" should {

    "return 0 for repository that stores nothing" withReqFor(endpoint + "/count") in {
      req =>
        // empty repository
        repository = Nil

        // serve the request and validate response
        assertEqualsNumber(serve(req), 0)
    }

    "return 1 for repository that stores one subject" withReqFor(endpoint + "/count") in {
      req =>
        // initialize service with exactly one subject
        repository = Nil
        repository ::= Subject(subjectId, "Subject", "This is a test.")
        repository.size mustEqual 1

        // serve the request and validate response
        assertEqualsNumber(serve(req), 1)
    }

    "return correct count for repository that stores many subjects" withReqFor(endpoint + "/count") in {
      req =>
        // initialize service with numSubjects subjects
        repository = Nil
        for (i <- 1 until numSubjects+1) {
          repository ::= Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        repository.size mustEqual numSubjects

        // serve the request and validate response
        assertEqualsNumber(serve(req), numSubjects)
    }

    "return nothing for path ending with a slash" withReqFor(endpoint + "/count/") in {
      req =>
        assertEmptyResponse(serve(req))
    }

    "return nothing for malformed path" withReqFor(endpoint + "/count/oopps") in {
      req =>
        assertEmptyResponse(serve(req))
    }

  }

  "GET /api/subject/<id>" should {

    "return nothing from empty repository" withReqFor(endpoint + "/" + subjectId) in {
      req =>
        // empty repository
        repository = Nil

        // serve the request and validate response
        assertEmptyResponse(serve(req))
    }

    "return nothing for non-existing id" withReqFor(endpoint + "/" + subjectId) in {
      req =>
        // initialize service with numSubjects-1 subjects
        repository = Nil
        for (i <- 1 until numSubjects+1) {
          if (i != subjectId)
            repository ::= Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        repository.size mustEqual numSubjects-1

        // serve the request and validate response
        assertEmptyResponse(serve(req))
    }

    "return single subject from repository that stores exactly one" withReqFor(endpoint + "/" + subjectId) in {
      req =>
        // initialize service with exactly one subject
        repository = Nil
        repository ::= Subject(subjectId, "Subject", "This is a test.")
        repository.size mustEqual 1

        // serve the request and validate response
        assertEqualsEntity(serve(req), service.repository(subjectId))
    }

    "return single subject from repository that stores many" withReqFor(endpoint + "/" + subjectId) in {
      req =>
        // initialize service with numSubjects subjects
        repository = Nil
        for (i <- 1 until numSubjects+1) {
          repository ::= Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        repository.size mustEqual numSubjects

        // serve the request and validate response
        assertEqualsEntity(serve(req), service.repository(subjectId))
    }

    "return nothing for invalid id" withReqFor(endpoint + "/not-an-id") in {
      req =>
        assertEmptyResponse(serve(req))
    }

    "return nothing for missing id" withReqFor(endpoint + "/") in {
      req =>
        assertEmptyResponse(serve(req))
    }

    "return nothing for malformed path" withReqFor(endpoint + "/foo/" + subjectId) in {
      req =>
        assertEmptyResponse(serve(req))
    }

  }

}
