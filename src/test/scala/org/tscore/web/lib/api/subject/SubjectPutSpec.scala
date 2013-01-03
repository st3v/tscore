package org.tscore.web.lib.api.subject

import org.tscore.web.lib.api.SubjectSpec
import net.liftweb.mocks.MockHttpServletRequest
import org.tscore.trust.model.Subject
import net.liftweb.http.js.JsExp
import net.liftweb.json.Extraction._

class SubjectPutSpec extends SubjectSpec {

  private val numSubjects = 99

  // some subjects to test with
  val subjectId = 99
  var subjectWithoutId = Subject(null, "Test1", "This is a test.")
  var subjectWithExplicitId = Subject(subjectId, "Test2", "This is a test.")
  var subjectWithoutDescription = Subject(null, "Test3", null)
  var subjectWithoutName = Subject(null, null, null)

  // empty repository
  repository = Nil

  "PUT /api/subject" should {

    "add new subject to an empty repository" withReqFor(request) withPut (subjectWithoutId) in {
      req =>
        // initialize service with empty list
        repository.size mustEqual 0

        // send request and verify
        assertEqualsEntity(serve(req), subjectWithoutId)

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual subjectWithoutId
    }

    "add new subject to a repository that already contains some entities" withReqFor(request) withPut (subjectWithoutId) in {
      req =>
        // initialize service with empty list
        for (i <- 1 until numSubjects+1) {
          repository ::= Subject(i, "Subject_"+i, "This is subject #"+i)
        }
        repository.size mustEqual numSubjects

        // send request and verify
        assertEqualsEntity(serve(req), subjectWithoutId)

        // verify repository after the request has been served
        repository.size mustEqual numSubjects + 1
        repository.head mustEqual subjectWithoutId
    }

    "add new subject without description" withReqFor(request) withPut (subjectWithoutDescription) in {
      req =>
      // initialize service with empty list
        repository.size mustEqual 0

        // send request and verify
        assertEqualsEntity(serve(req), subjectWithoutDescription)

        // verify repository after the request has been served
        repository.size mustEqual 1
        repository.head mustEqual subjectWithoutDescription
    }

    "ignore explicitly set id in json payload" withReqFor(request) withPut (subjectWithExplicitId) in {
      req =>
        // initialize service with empty list
        repository.size mustEqual 0

        // send request and verify
        assertNotEqualsEntity(serve(req), subjectWithExplicitId)

        // verify repository after the request has been served
        repository.size mustEqual 1

        // subject in repository should have different Id
        repository.head.id mustNotEqual subjectWithExplicitId.id

        // otherwise they should be the same
        subjectWithExplicitId.id = repository.head.id
        repository.head mustEqual subjectWithExplicitId
    }

    "return nothing if id set in path" withReqFor(endpoint + "/" + subjectId) withPut (subjectWithoutId) in {
      req =>
        // initialize service with empty list
        repository.size mustEqual 0

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 0
    }

    "return nothing subject without name" withReqFor(request) withPut (subjectWithoutName) in {
      req =>
        // initialize service with empty list
        repository.size mustEqual 0

        // send request and verify
        assertEmptyResponse(serve(req))

        // verify repository after the request has been served
        repository.size mustEqual 0
    }
  }

  /**
   * Override parent method in order to set the id of a subject to the maxId inside the repository.
   * This way we can assert equality on newly inserted subjects.
   *
   * @param jsExp   JsExp value to compare.
   * @param entity  Resource entity to compare against.
   * @return        Specs2 result.
   */

  override protected def assertEqualsEntity(jsExp: JsExp, entity: Subject): org.specs2.execute.Result = {
    if (entity.id == null) {
      entity.id = service.maxId
    }
    super.assertEqualsEntity(jsExp, entity)
  }
}
