package org.tscore.trust.model

import org.tscore.trust.model.score.TrustScore
import org.tscore.trust.Util._

class Actor extends org.tscore.graph.model.Actor  {
  var score: TrustScore = _
}

object Actor {
  implicit def toActor[U <: AnyRef](obj: U): Actor = safeCast[Actor](obj)

  def apply(name: String, score: TrustScore) = {
    val actor = new Actor
    actor.name = name
    actor.score = score
    actor
  }
}