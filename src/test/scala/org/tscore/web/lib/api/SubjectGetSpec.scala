package org.tscore.web.lib.api

import net.liftweb.mocks.MockHttpServletRequest
import net.liftweb.common.{Empty, Full}
import net.liftweb.json.JsonAST._
import net.liftweb.http.{LiftResponse, Req, JsonResponse}
import net.liftweb.http.js.JsExp._
import org.tscore.web.model.Subject
import net.liftweb.http.js.JsExp
import org.tscore.web.model.Subject
import net.liftweb.json.JsonAST._
import net.liftweb.json.Printer._
import net.liftweb.json.Extraction._
import net.liftweb.json.JsonParser

class SubjectGetSpec extends ApiSpec {
  implicit val formats = net.liftweb.json.DefaultFormats

  val baseUrl = "http://tscore.org/api/subject"
  var request = new MockHttpServletRequest(baseUrl)

  val numSubjects = 99
  val findSubjectId = 17

  implicit def toSubject(in: org.tscore.trust.model.Subject): Subject = {
    Subject(Option(in.id), in.name, Option(in.description))
  }

  implicit def toSubjectList(in: List[org.tscore.trust.model.Subject]): List[Subject] = {
    in.map(toSubject)
  }

  implicit def fromSubject(in: Subject): org.tscore.trust.model.Subject = {
    org.tscore.trust.model.Subject(
      in.id.getOrElse(null.asInstanceOf[Long]),
      in.name,
      in.description.getOrElse(null))
  }

  private def assertEmptyList(json: JsExp) = {
    assertEqualsList(json, List[Subject]())
  }

  private def assertEqualsList(json: JsExp, list: List[Subject]) = {
    JsonParser.parse(json.toJsCmd) mustEqual decompose(list)
  }

  private def assertEqualsSubject(json: JsExp, subject: Subject) = {
    JsonParser.parse(json.toJsCmd) mustEqual decompose(subject)
  }

  private def assertEmptyResponse(request: Req) =
    SubjectRoutes(request).apply() match {
      case Empty => {
        success
      }
      case _ => {
        failure("Not an empty response.")
      }
  }

  private def serveJson(request: Req) =
    SubjectRoutes(request).apply() match {
      case Full(resp: JsonResponse) => {
        resp.code mustEqual 200
        resp.json
      }
      case _ => {
        failure("Invalid JsonResponse.")
      }
  }

  "GET /api/subject" should {

    "return empty list" withReqFor(request) in {
      req =>
        // initialize service with empty list
        subjectService.subjects = Nil
        subjectService.subjects.size mustEqual 0

        // serve the request and validate response
        assertEmptyList(serveJson(req))
    }

    "return list with one subject" withReqFor(request) in {
      req =>
        // initialize service with exactly one subject
        val subject = Subject(Some(99), "Stev", Some("This is a test"))
        subjectService.subjects = List(subject)
        subjectService.subjects.size mustEqual 1

        // serve the request and validate response
        assertEqualsList(serveJson(req), subjectService.subjects)
    }

    "return list with multiple subjects" withReqFor(request) in {
      req =>
        // initialize service with exactly one subject
        subjectService.subjects = Nil
        for (i <- 1 until numSubjects+1) {
          subjectService.subjects ::= Subject(Some(i), "Subject_"+i, Some("This is subject #"+i))
        }
        subjectService.subjects.size mustEqual numSubjects

        // serve the request and validate response
        assertEqualsList(serveJson(req), subjectService.subjects)
    }

    "return nothing from repository that stores nothing" withReqFor(baseUrl + "/" + findSubjectId) in {
      req =>
        // initialize service with exactly one subject
        subjectService.subjects = Nil

        // serve the request and validate response
        assertEmptyResponse(req)
    }

    "return single subject from repository that stores exactly one" withReqFor(baseUrl + "/" + findSubjectId) in {
      req =>
        // initialize service with exactly one subject
        subjectService.subjects = Nil
        subjectService.subjects ::= Subject(Some(findSubjectId), "Subject", Some("This is a test."))
        subjectService.subjects.size mustEqual 1

        // serve the request and validate response
        assertEqualsSubject(serveJson(req), subjectService.subjects.find(_.id == findSubjectId).get)
    }

    "return single subject from repository that stores many" withReqFor(baseUrl + "/" + findSubjectId) in {
      req =>
        // initialize service with exactly one subject
        subjectService.subjects = Nil
        for (i <- 1 until numSubjects+1) {
          subjectService.subjects ::= Subject(Some(i), "Subject_"+i, Some("This is subject #"+i))
        }
        subjectService.subjects.size mustEqual numSubjects

        // serve the request and validate response
        assertEqualsSubject(serveJson(req), subjectService.subjects.find(_.id == findSubjectId).get)
    }

//    "return a non-empty list" withReqFor(request) in {
//      req =>
//        // empty mock repository
//        SubjectServiceMock.subjects = Nil
//
//        // serve the request and validate response
//        SubjectRoutes(req).apply() match {
//          case Full(resp: JsonResponse) => {
//            resp.code mustEqual 200
//            resp.json mustEqual jValueToJsExp(Seq[Subject](): JValue)
//          }
//          case _ => failure("Not a valid JsonResponse.")
//        }
//    }

  }
}
