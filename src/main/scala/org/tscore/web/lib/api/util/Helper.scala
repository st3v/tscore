package org.tscore.web.lib.api.util

import util.control.Exception._
import net.liftweb.json.JsonAST.{JObject, JValue}

object Helper {
  object Id {
    def unapply(in: String) = catching(classOf[Exception]).opt(in.toLong)
  }

  object JsonWithoutId {
    def unapply(in: JValue) = Some(in.transform {
      case JObject(xs) => JObject(xs.filterNot(_.name == "id"))
    })
  }
}
