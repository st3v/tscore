package org.tscore.web.lib.api

import org.tscore.web.WebSpec2
import org.springframework.context.support.GenericXmlApplicationContext
import org.tscore.web.lib.service.ServiceMock
import net.liftweb.http.js.JsExp
import net.liftweb.json.Extraction._
import net.liftweb.json._
import net.liftweb.http.{JsonResponse, Req}
import net.liftweb.common.Empty
import net.liftweb.http.rest.RestHelper
import net.liftweb.common.Full
import net.liftweb.json.JsonAST.JValue
import scala.collection.JavaConversions._

/**
 * Super class for API tests.  Provides ways to serve requests, handy assertions,
 * and access to a mocked-out repository that stores the resource entities for the REST endpoint.
 *
 * @param routes      The RestHelper object that defines the routes for the endpoint under test.
 * @param serializer  Custom JSON serializer for resource under test. Can be omitted if
 *                    the resource doesn't require a special serializer (e.g. the resource
 *                    is a primitive or a case class)
 *
 * @tparam T          Type of the Resource under test.
 */
abstract class ApiSpec[T](val routes: RestHelper, val serializer: Serializer[T] = null)(implicit m: Manifest[T])
  extends WebSpec2(new bootstrap.liftweb.Boot().boot _)
  with ApiSpecContext[T] {

  // run API tests sequentially
  override def is = args(sequential = true) ^ super.is

  // need this for JValue decomposition
  implicit var formats: Formats = net.liftweb.json.DefaultFormats

  // if defined, add the custom serializer to the implicit formats
  if (serializer != null) {
    formats = formats + serializer
  }

  // get the service mock for the resource under test
  for (bean <- ctx.getBeansOfType(classOf[ServiceMock[T]]).entrySet) {
    if (m == bean.getValue.resourceManifest) {
      service = bean.getValue
    }
  }

  /**
   * Asserts that passed JsExp value equals passed number.
   *
   * @param jsExp   JsExp value to compare.
   * @param number  Primitive number to compare against.
   * @return        Specs2 result.
   */
  protected def assertEqualsNumber[N](jsExp: JsExp, number: N)
                                     (implicit num: Numeric[N]): org.specs2.execute.Result = {
    jsExp.toJsCmd.toDouble mustEqual num.toDouble(number)
  }

  /**
   * Assert that passed JsExp equals passed JValue.
   *
   * @param jsExp   JsExp value to compare.
   * @param jValue  JValue to compare against.
   * @return        Specs2 result.
   */
  protected def assertEqualsJValue(jsExp: JsExp, jValue: JValue): org.specs2.execute.Result = {
    JsonParser.parse(jsExp.toJsCmd) mustEqual jValue
  }

  /**
   * Asserts that passed JsExp equals passed resource entity.
   *
   * @param jsExp   JsExp value to compare.
   * @param entity  Resource entity to compare against.
   * @return        Specs2 result.
   */
  protected def assertEqualsEntity(jsExp: JsExp, entity: T): org.specs2.execute.Result = {
    assertEqualsJValue(jsExp, decompose(entity))
  }

  /**
   * Asserts that passed JsExp value equals passed list
   *
   * @param jsExp  JsExp value to compare.
   * @param list   List to compare against.
   * @return       Specs2 result.
   */
  protected def assertEqualsEntityList(jsExp: JsExp, list: List[T]): org.specs2.execute.Result = {
    assertEqualsJValue(jsExp, decompose(list))
  }

  /**
   * Asserts that passed JsExp value equals empty list.
   *
   * @param jsExp  JsExp value to compare.
   * @return       Specs2 result.
   */
  protected def assertEmptyEntityList(jsExp: JsExp): org.specs2.execute.Result = {
    assertEqualsEntityList(jsExp, List[T]())
  }

  /**
   * Serve the passed request and assert that the resulting response is empty.
   *
   * @param request  Request to be served.
   * @return         Specs2 result.
   */
  protected def assertEmptyResponse(request: Req): org.specs2.execute.Result =
    routes(request).apply() match {
      case Empty => {
        success
      }
      case _ => {
        failure("Not an empty response.")
      }
    }

  /**
   * Serve the passed request and assert that the resulting response is a valid JsonResponse.
   *
   * @param request  Request to be served.
   * @return         Response payload as JsExp.
   */
  protected def serveJson(request: Req): JsExp =
    routes(request).apply() match {
      case Full(resp: JsonResponse) => {
        resp.code mustEqual 200
        resp.json
      }
      case _ => {
        failure("Invalid JsonResponse.")
      }
    }
}

/**
 * Spec Context that holds the subject repository.
 *
 * @tparam T  Type of the Resource under test.
 */
trait ApiSpecContext[T] {
  // load application context
  protected val ctx = new GenericXmlApplicationContext("classpath*:/META-INF/spring/module-context-test-web.xml")

  protected var service: ServiceMock[T] = null

  // repository delegate getter
  protected def repository: List[T] = {
    if (service != null) {
      service.repository
    }
    else {
      Nil
    }
  }

  // repository delegate setter
  protected def repository_=(list: List[T]) {
    if (service != null) service.repository = list
  }
}
