package org.tscore.trust.model.score

import org.tscore.graph.model.Score

object NumericScore {
  implicit def toNumericScore[T](value: T)(implicit num: Numeric[T])= new NumericScore(value)
  implicit def fromNumericScore[T](score: NumericScore[T]) = score.asInstanceOf[T]
}

class NumericScore[T] (val value: T) (implicit num: Numeric[T]) extends Score with Numeric[NumericScore[T]] with Ordered[NumericScore[T]] {
  def fromInt (n: Int) = new NumericScore(num.fromInt(n))
  def plus (x: NumericScore[T], y: NumericScore[T]) = new NumericScore(num.plus(x, y))
  def minus (x: NumericScore[T], y: NumericScore[T]) = new NumericScore(num.minus(x,y))
  def times (x: NumericScore[T], y: NumericScore[T]) = new NumericScore(num.times(x, y))
  def negate (x: NumericScore[T]) = new NumericScore(num.negate(x))
  def toInt (x: NumericScore[T]) = num.toInt(x)
  def toLong (x: NumericScore[T]) = num.toLong(x)
  def toFloat (x: NumericScore[T]) = num.toFloat(x)
  def toDouble (x: NumericScore[T]) = num.toDouble(x)
  def compare (x: NumericScore[T], y: NumericScore[T]) = num.compare(x.value, y.value)
  def compare(that: NumericScore[T]) = compare(this, that)
  override def toString: String = "%s".format(value)
  override def equals(obj:Any) = {
    obj match {
      case y: NumericScore[_] => y.value == value
      case _ => obj == value
    }
  }
}

