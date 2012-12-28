package org.tscore.trust.model.score

case class EndorsementScore(override val value: Int) extends NumericScore[Int](value)

object EndorsementScore {
  implicit def toEndorsementScore(value: Int) = new EndorsementScore(value)
  implicit def fromTrustScore(score: TrustScore) = score.asInstanceOf[Int]
}
