package org.tscore.web.lib.api

import net.liftweb.http.rest.RestHelper
import net.liftweb.http.S
import net.liftweb.json.JInt
import net.liftweb.json.JsonAST.{JObject, JValue}
import org.tscore.web.model.Actor
import org.tscore.web.lib.controller.ActorController
import util.Helper._
import net.liftweb.common.{Empty, Full}

object ActorRoutes extends RestHelper {

  /**
   * Serve /api/actor
   */
  serve( handler = "api" / "actor" prefix {

    /**
     * GET /actor
     *
     * Returns a list of all actors stored in the repository.  The returned list
     * will be empty if no actors exists in the repository.
     */
    case Nil JsonGet _ => ActorController.allActors: JValue

    /**
     * GET /actor/count
     *
     * Returns the number of actors stored in the repository.
     */
    case "count" :: Nil JsonGet _ => JInt(ActorController.allActors.length)

    /**
     * GET /actor/search/<query_string>
     *   or
     * GET /actor/search?q=<query_string>
     *
     * Returns a list of actors whose description contains the specified <query_string>.
     * If no such actor exists, the returned list will be empty.
     */
    case "search" :: q JsonGet _ =>
      (for {
        searchString <- q ::: S.params("q")
        actor <- ActorController.search(searchString)

      } yield actor
        ).distinct: JValue

    /**
     * GET /actor/<id>
     *
     * If <id> is a proper ID this route returns the actor with that ID if it exists.
     * Otherwise it returns a 404.
     */
    case Id(id) :: Nil JsonGet _ => ActorController.find(id).map(a => a: JValue)

    /**
     * DELETE /actor/<id>
     *
     * Deletes the actor specified by ID.
     *
     * Returns the deleted actor, if a actor with <id> existed. Otherwise this returns a 404.
     */
    case Id(id) :: Nil JsonDelete _ => ActorController.delete(id).map(a => a: JValue)

    /**
     * PUT /actor
     *
     * Adds a actor defined by the JSON in the PUT body.
     * Note, we strip any ID field from the JSON passed in the request body.
     *
     * Returns the new actor if it was stored successfully. Otherwise this returns a 404.
     */
    case Nil JsonPut JsonWithoutId(json) -> _ =>
      (Actor(json) match {
        case Some(actor) => ActorController.add(actor)
        case _ => Empty
      }).map(a => a: JValue)

    /**
     * POST /actor/<id>
     *
     * Updates actor with <id>. New properties are specified as JSON in the POST body.
     * Note, we strip any ID field from the JSON passed in the request body.
     *
     * Returns the updated actor if the update was successful. Otherwise this returns a 404.
     */
    case Id(id) :: Nil JsonPost JsonWithoutId(json) -> _ =>
      (ActorController.find(id) match {                                   // find existing resource and cast it to JSON
        case Full(actor) => Actor(                                        // create bean from the merged JSON
          mergeJson(actor,json)                                           // merge existing JSON w/ the new one
        ).map(ActorController.add(_).get)                                 // store the updated resource and cast result
        case _ => None
      }).map(a => a: JValue)
  })
}
