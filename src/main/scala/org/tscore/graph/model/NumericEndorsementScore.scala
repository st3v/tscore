package org.tscore.graph.model

object NumericEndorsementScore {
  implicit def toNumericScore[U](value: U)(implicit n: Numeric[U]) = new NumericEndorsementScore(value)
  implicit def fromNumericScore[U](score: NumericEndorsementScore[U]) = score.value
}

class NumericEndorsementScore[T] (v: T)(implicit n: Numeric[T]) extends EndorsementScore[NumericEndorsementScore[T]] {
  val value = v

  def +(that: NumericEndorsementScore[T]) = n.plus(this, that)

  def -(that: NumericEndorsementScore[T]) = n.minus(this, that)

  def equiv(that: Any) : Boolean = try {n.equiv(this, that.asInstanceOf[T])} catch {case e:Exception => false}

  def <(that: NumericEndorsementScore[T]) = n.lt(this, that)

  def <=(that: NumericEndorsementScore[T]) = n.lteq(this, that)

  def >(that: NumericEndorsementScore[T]) = n.gt(this, that)

  def >=(that: NumericEndorsementScore[T]) = n.gteq(this, that)

  def isPositive = n.gt(this, n.zero)

  def isNegative = n.lt(this, n.zero)

  override def toString : String = value.toString

  override def equals(obj:Any) = {
    obj match {
      case y: this.type => y.value == this.value
      case _ => obj.equals(this.value)
    }
  }

}

class NumericEndorsementScoreFactory[AnyRef] extends EndorsementScoreFactory[AnyRef] {
  def createScoreFromString(value: String) = new NumericEndorsementScore(value.toDouble)
}

