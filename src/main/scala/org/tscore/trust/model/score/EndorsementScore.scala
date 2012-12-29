package org.tscore.trust.model.score

case class EndorsementScore(override val value: Int) extends NumericScore[Int](value) with TrustScore

object EndorsementScore {
  implicit def toEndorsementScore(value: Int) = new EndorsementScore(value)
  implicit def fromEndorsementScore(score: EndorsementScore): Int = score.value

  def zero = new EndorsementScore(0)
  def default = zero
  def empty = zero
}
