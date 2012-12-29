package org.tscore.trust.model.score

case class ActorScore(override val value: Double) extends NumericScore[Double](value) with TrustScore

object ActorScore {
  implicit def doubleToActorScore(value: Double) = new ActorScore(value)
  implicit def doubleFromActorScore(score: ActorScore) = score.value

  def zero = new ActorScore(0)
  def default = zero
  def empty = zero

}

