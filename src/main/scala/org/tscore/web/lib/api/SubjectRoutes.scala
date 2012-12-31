package org.tscore.web.lib.api

import net.liftweb.http.rest.RestHelper
import net.liftweb.http.S
import net.liftweb.json.JInt
import net.liftweb.json.JsonAST.{JObject, JValue}
import org.tscore.web.model.Subject
import org.tscore.web.lib.controller.SubjectController
import util.Helper._
import net.liftweb.common.{Box, Empty, Full}
import net.liftweb.util.BasicTypesHelpers.AsLong

object SubjectRoutes extends RestHelper {

  /**
   * Serve /api/subject
   */
  serve( handler = "api" / "subject" prefix {

    /**
     * GET /subject
     *
     * Returns a list of all subjects stored in the repository.  The returned list
     * will be empty if no subjects exists in the repository.
     */
    case Nil JsonGet _ => SubjectController.allSubjects: JValue

    /**
     * GET /subject/count
     *
     * Returns the number of subjects stored in the repository.
     */
    case "count" :: Nil JsonGet _ => JInt(SubjectController.allSubjects.length)

    /**
     * GET /subject/search/<query_string>
     *   or
     * GET /subject/search?q=<query_string>
     *
     * Returns a list of subjects whose description contains the specified <query_string>.
     * If no such subject exists, the returned list will be empty.
     */
    case "search" :: q JsonGet _ =>
      (for {
        searchString <- q ::: S.params("q")
        subject <- SubjectController.search(searchString)

      } yield subject
        ).distinct: JValue

    /**
     * GET /subject/<id>
     *
     * If <id> is a proper ID this route returns the subject with that ID if it exists.
     * Otherwise it returns a 404.
     */
    case Id(id) :: Nil JsonGet _ => SubjectController.find(id).map(a => a: JValue)

    /**
     * DELETE /subject/<id>
     *
     * Deletes the subject specified by ID.
     *
     * Returns the deleted subject, if a subject with <id> existed. Otherwise this returns a 404.
     */
    case AsLong(id) :: Nil JsonDelete _ => SubjectController.delete(id).map(a => a: JValue)

    /**
     * PUT /subject
     *
     * Adds a subject defined by the JSON in the PUT body.
     * Note, we strip any ID field from the JSON passed in the request body.
     *
     * Returns the new subject if it was stored successfully. Otherwise this returns a 404.
     */
    case Nil JsonPut JsonWithoutId(json) -> _ =>
      (Subject(json) match {
        case Some(subject) => SubjectController.add(subject)
        case _ => Empty
      }).map(a => a: JValue)

    /**
     * POST /subject/<id>
     *
     * Updates subject with <id>. New properties are specified as JSON in the POST body.
     * Note, we strip any ID field from the JSON passed in the request body.
     *
     * Returns the updated subject if the update was successful. Otherwise this returns a 404.
     */
    case Id(id) :: Nil JsonPost JsonWithoutId(json) -> _ =>
      (SubjectController.find(id) match {
        case Full(subject) =>
          (Subject(mergeJson(subject, json)) match {
            case Some(s) => SubjectController.add(s)
            case _ => Empty
          })
        case _ => Empty
      }).map(a => a: JValue)

  })
}
