package org.tscore.web.lib.api

import org.tscore.trust.model.Subject
import net.liftweb.json.CustomSerializer
import net.liftweb.json.JsonAST._

class SubjectSpec extends ApiSpec[Subject](SubjectRoutes, SubjectSerializer)

object SubjectSerializer extends CustomSerializer[Subject] (formats => (
  {
    case JObject(
    JField("id", JInt(id)) ::
      JField("name", JString(name)) ::
      JField("description", JString(desc)) :: Nil) => Subject(id.toLong, name, desc)
  },
  {
    case s: Subject => JObject(
      JField("id", JInt(s.id.toInt)) ::
        JField("name", JString(s.name)) ::
        JField("description", JString(s.description))
        :: Nil)
  }
))
