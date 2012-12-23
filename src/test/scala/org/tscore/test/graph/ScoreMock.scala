package org.tscore.test.graph

import org.tscore.graph.model.Score

case class ScoreMock(value: Double) extends Score {
  override def equals(obj:Any) = {
    obj match {
      case y: ScoreMock => y.value == value
      case _ => obj == value
    }
  }
}

object ScoreMock {
  implicit def toTestScore(value: Double) = new ScoreMock(value)
  implicit def fromTestScore(score: ScoreMock) = score.value
}
