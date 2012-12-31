package org.tscore.web.lib.api

import net.liftweb.http.rest.RestHelper
import net.liftweb.json.JInt
import net.liftweb.json.JsonAST.{JObject, JValue}
import org.tscore.web.model.{Subject, Endorsement}
import org.tscore.web.lib.controller.{SubjectController, EndorsementController}
import util.Helper._
import net.liftweb.common.{Empty, Full}

object EndorsementRoutes extends RestHelper {

  /**
   * Serve /api/endorsement
   */
  serve( handler = "api" / "endorsement" prefix {

    /**
     * GET /endorsement
     *
     * Returns a list of all endorsements stored in the repository.  The returned list
     * will be empty if no endorsements exists in the repository.
     */
    case Nil JsonGet _ => EndorsementController.allEndorsements: JValue

    /**
     * GET /endorsement/count
     *
     * Returns the number of endorsements stored in the repository.
     */
    case "count" :: Nil JsonGet _ => JInt(EndorsementController.allEndorsements.length)

    /**
     * GET /endorsement/<id>
     *
     * If <id> is a proper ID this route returns the endorsement with that ID if it exists.
     * Otherwise it returns a 404.
     */
    case Id(id) :: Nil JsonGet _ => EndorsementController.find(id).map(a => a: JValue)

    /**
     * DELETE /endorsement/<id>
     *
     * Deletes the endorsement specified by ID.
     *
     * Returns the deleted endorsement, if a endorsement with <id> existed. Otherwise this returns a 404.
     */
    case Id(id) :: Nil JsonDelete _ => EndorsementController.delete(id).map(a => a: JValue)

    /**
     * PUT /endorsement
     *
     * Adds a endorsement defined by the JSON in the PUT body.
     * Note, we strip any ID field from the JSON passed in the request body.
     *
     * Returns the new endorsement if it was stored successfully. Otherwise this returns a 404.
     */
    case Nil JsonPut JsonWithoutId(json) -> _ =>
      (Endorsement(json) match {
        case Some(endorsement) => EndorsementController.add(endorsement)
        case _ => Empty
      }).map(a => a: JValue)

    /**
     * POST /endorsement/<id>
     *
     * Updates endorsement with <id>. New properties are specified as JSON in the POST body.
     * Note, we strip any ID field from the JSON passed in the request body.
     *
     * Returns the updated endorsement if the update was successful. Otherwise this returns a 404.
     */
    case Id(id) :: Nil JsonPost JsonWithoutId(json) -> _ =>
      (EndorsementController.find(id) match {                                   // find existing resource and cast it to JSON
        case Full(endorsement) => Endorsement(                                  // create bean from the merged JSON
          mergeJson(endorsement,json)                                           // merge existing JSON w/ the new one
        ).map(EndorsementController.add(_).get)                                 // store the updated resource and cast result
        case _ => None
      }).map(a => a: JValue)

  })
}
