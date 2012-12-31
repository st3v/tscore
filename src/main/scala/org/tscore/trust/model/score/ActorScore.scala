package org.tscore.trust.model.score

case class ActorScore(override val value: Double) extends NumericScore[Double](value) with TrustScore

object ActorScore {
  implicit def doubleToActorScore(value: Double) = new ActorScore(value)

  implicit def doubleFromActorScore(score: ActorScore) = Option(score) match {
    case Some(s) => s.value
    case _ => default.value
  }

  def zero = new ActorScore(0)
  def default = zero
  def empty = zero

}

