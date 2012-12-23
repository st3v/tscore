package org.tscore.trust

import org.tscore.graph.model.Actor
import org.tscore.trust.score.TrustScore
import org.tscore.trust.score.TrustScore._

class TrustActor extends Actor {
  var score: TrustScore = 1.0
}

object TrustActor {
  def apply(name: String, score: TrustScore) = {
    val actor = new TrustActor()
    actor.name = name
    actor.score = score
    actor
  }

  def apply(name: String) = {
    val actor = new TrustActor()
    actor.name = name
    actor
  }
}
