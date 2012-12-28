package org.tscore.trust.model.score

case class TrustScore(override val value: Double) extends NumericScore[Double](value)

object TrustScore {
  implicit def doubleToTrustScore(value: Double) = new TrustScore(value)
  implicit def doubleFromTrustScore(score: TrustScore) = score.value

  def zero = new TrustScore(0)
  def empty = zero

}

