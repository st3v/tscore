package org.tscore.web.lib.api

import org.tscore.trust.model.Actor
import net.liftweb.json._
import net.liftweb.json.JsonAST.JField
import net.liftweb.json.JsonAST.JNull
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonAST.JObject
import net.liftweb.json.JsonAST.JString
import net.liftweb.json.JsonAST.JInt
import net.liftweb.mocks.MockHttpServletRequest

class ActorSpec extends ApiSpec[Actor](ActorRoutes, ActorSerializer) {
  val endpoint = "http://tscore.org/api/actor"

  // important that this is a function and not a variable,
  // this way we create a new mock every time we reference request
  // which prevents side-effects between the spec examples
  protected def request = new MockHttpServletRequest(endpoint)

  formats = ActorFormats
}

object ActorFormats extends DefaultFormats {
  override val typeHintFieldName = "__type__"
  override val customSerializers = List(ActorSerializer)
}

object ActorSerializer extends Serializer[Actor] {
  private val Class = classOf[Actor]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Actor] = {
    case (TypeInfo(Class, _), json) =>
      Actor(
        json.\("id").extract[java.lang.Long],
        json.\("name").extract[String],
        json.\("description").extract[String],
        json.\("score").extract[Double]
      )
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case a: Actor => JObject(
      //JField(format.typeHintFieldName, JString(classOf[Subject].getCanonicalName)) ::
      JField("id", if(a.id == null) JNull else JInt(a.id.toInt)) ::
      JField("name", if(a.name == null) JNull else JString(a.name)) ::
      JField("description", if (a.description == null) JNull else JString(a.description)) ::
      JField("score", if (a.score == null) JNull else JDouble(a.score)) :: Nil)
  }
}
