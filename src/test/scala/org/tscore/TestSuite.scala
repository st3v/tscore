package org.tscore

import org.scalatest.FunSuite

trait TestSuite extends FunSuite {
  override def suiteName = this.getClass.getName
}
