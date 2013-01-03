package org.tscore.web.lib.api

import org.tscore.trust.model.Subject
import net.liftweb.json._
import net.liftweb.json.JsonAST.JField
import net.liftweb.json.JsonAST.JNull
import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonAST.JObject
import net.liftweb.json.JsonAST.JString
import net.liftweb.json.JsonAST.JInt
import net.liftweb.mocks.MockHttpServletRequest

class SubjectSpec extends ApiSpec[Subject](SubjectRoutes, SubjectSerializer) {
  val endpoint = "http://tscore.org/api/subject"

  // important that this is a function and not a variable,
  // this way we create a new mock every time we reference request
  // which prevents side-effects between the spec examples
  protected def request = new MockHttpServletRequest(endpoint)

  formats = SubjectFormats
}

object SubjectFormats extends DefaultFormats {
  override val typeHintFieldName = "__type__"
  override val customSerializers = List(SubjectSerializer)
}

object SubjectSerializer extends Serializer[Subject] {
  private val Class = classOf[Subject]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Subject] = {
    case (TypeInfo(Class, _), json) =>
      Subject(
        json.\("id").extract[java.lang.Long],
        json.\("name").extract[String],
        json.\("description").extract[String]
      )
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case s: Subject => JObject(
      //JField(format.typeHintFieldName, JString(classOf[Subject].getCanonicalName)) ::
      JField("id", if(s.id == null) JNull else JInt(s.id.toInt)) ::
      JField("name", if(s.name == null) JNull else JString(s.name)) ::
      JField("description", if (s.description == null) JNull else JString(s.description)) :: Nil)
  }
}
