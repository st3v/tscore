package org.tscore.graph.model

trait EndorsementScore[T] {

  def +(that: T) : T

  def -(that: T) : T

  def equiv(that: Any) : Boolean

  def <(that: T) : Boolean

  def <=(that: T) : Boolean

  def >(that: T) : Boolean

  def >=(that: T) : Boolean

  def isPositive: Boolean

  def isNegative: Boolean

  override def equals(obj : Any) : Boolean = this.equiv(obj)

  def toString : String
}


