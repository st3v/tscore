package org.tscore.trust.service

trait CrudService[T] {
  def getAll: Seq[T]
  def find(id: Long): Option[T]
  def add(entity: T): Option[T]
  def delete(id: Long): Option[T]
  def prependListener(f: T => Unit)
}
