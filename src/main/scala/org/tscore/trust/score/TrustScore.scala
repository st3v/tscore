package org.tscore.trust.score

case class TrustScore(override val value: Double) extends NumericScore[Double](value)

object TrustScore {
  implicit def doubleToTrustScore(value: Double) = new TrustScore(value)
  implicit def doubleFromTrustScore(score: TrustScore) = score.asInstanceOf[Double]
}

