package org.tscore.web.lib.api.subject

import org.tscore.web.lib.api.SubjectSpec
import net.liftweb.mocks.MockHttpServletRequest
import org.tscore.trust.model.Subject


class GetSpec extends SubjectSpec {
  private val baseUrl = "http://tscore.org/api/subject"
  private var request = new MockHttpServletRequest(baseUrl)

  private val numSubjects = 99
  private val findSubjectId = 17

  "GET /api/subject" should {

    "return empty list" withReqFor(request) in {
      req =>
        // initialize service with empty list
        repository = Nil
        repository.size mustEqual 0

        // serve the request and validate response
        assertEmptyEntityList(serveJson(req))
    }

    "return list with one subject" withReqFor(request) in {
      req =>
        // initialize service with exactly one subject
        repository = List(Subject(99, "Stev", "This is a test"))
        repository.size mustEqual 1

        // serve the request and validate response
        assertEqualsEntityList(serveJson(req), repository)
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
        assertEqualsEntityList(serveJson(req), repository)
    }

  }

  "GET /api/subject/count" should {

    "return zero for repository that stores nothing" withReqFor(baseUrl + "/count") in {
      req =>
        // empty repository
        repository = Nil

        // serve the request and validate response
        assertEqualsNumber(serveJson(req), 0)
    }

    "return one for repository that stores one subject" withReqFor(baseUrl + "/count") in {
      req =>
        // initialize service with exactly one subject
        repository = Nil
        repository ::= Subject(findSubjectId, "Subject", "This is a test.")
        repository.size mustEqual 1

        // serve the request and validate response
        assertEqualsNumber(serveJson(req), 1)
    }

    "return correct count for repository that stores many subjects" withReqFor(baseUrl + "/count") in {
      req =>
        // initialize service with numSubjects subjects
        repository = Nil
        for (i <- 1 until numSubjects+1) {
          repository ::= Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        repository.size mustEqual numSubjects

        // serve the request and validate response
        assertEqualsNumber(serveJson(req), numSubjects)
    }

    "throw exception for path ending with a slash" withReqFor(baseUrl + "/count/") in {
      req =>
        // should throw exception
        serveJson(req) must throwA[java.util.NoSuchElementException]
    }

    "throw exception for malformed path" withReqFor(baseUrl + "/count/oopps") in {
      req =>
        // should throw exception
        serveJson(req) must throwA[java.util.NoSuchElementException]
    }

  }

  "GET /api/subject/<id>" should {

    "return nothing from repository that stores nothing" withReqFor(baseUrl + "/" + findSubjectId) in {
      req =>
        // empty repository
        repository = Nil

        // serve the request and validate response
        assertEmptyResponse(req)
    }

    "return nothing from repository that stores something" withReqFor(baseUrl + "/" + findSubjectId) in {
      req =>
        // initialize service with numSubjects-1 subjects
        repository = Nil
        for (i <- 1 until numSubjects+1) {
          if (i != findSubjectId)
            repository ::= Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        repository.size mustEqual numSubjects-1

        // serve the request and validate response
        assertEmptyResponse(req)
    }

    "return single subject from repository that stores exactly one" withReqFor(baseUrl + "/" + findSubjectId) in {
      req =>
        // initialize service with exactly one subject
        repository = Nil
        repository ::= Subject(findSubjectId, "Subject", "This is a test.")
        repository.size mustEqual 1

        // serve the request and validate response
        assertEqualsEntity(serveJson(req), repository.find(_.id == findSubjectId).get)
    }

    "return single subject from repository that stores many" withReqFor(baseUrl + "/" + findSubjectId) in {
      req =>
        // initialize service with numSubjects subjects
        repository = Nil
        for (i <- 1 until numSubjects+1) {
          repository ::= Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        repository.size mustEqual numSubjects

        // serve the request and validate response
        assertEqualsEntity(serveJson(req), repository.find(_.id == findSubjectId).get)
    }

    "throw exception for invalid id" withReqFor(baseUrl + "/not-an-id") in {
      req =>
        // should throw exception
        serveJson(req) must throwA[java.util.NoSuchElementException]
    }

    "throw exception for missing id" withReqFor(baseUrl + "/") in {
      req =>
        // should throw exception
        serveJson(req) must throwA[java.util.NoSuchElementException]
    }

  }

}
