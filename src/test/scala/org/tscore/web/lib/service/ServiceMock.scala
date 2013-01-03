package org.tscore.web.lib.service

import collection.mutable
import org.tscore.trust.service.CrudService

class ServiceMock[T <: {var id: java.lang.Long}] (implicit val m: Manifest[T]) extends CrudService[T] {
  var repository = mutable.HashMap[Long, T]()
  var listeners: List[T => Unit] = Nil

  def getAll: Seq[T] = repository.values.toSeq.sortWith((e1:T, e2:T) => e1.id > e2.id)

  def find(id: Long): Option[T] = synchronized {
    repository.get(id)
  }

  def add(entity: T): Option[T] = synchronized {
    if (entity.id == null) {
      entity.id = maxId+1
    }
    repository(entity.id) = entity
    Option(updateListeners(entity))
  }

  def delete(id: Long): Option[T] = synchronized {
    val result = repository.remove(id)
    if (result.isDefined) updateListeners(result.get)
    result
  }

  def prependListener(f: T => Unit) {
    listeners ::= f
  }

  private def updateListeners(entity: T) = {
    synchronized {
      listeners.foreach(f => f(entity))
    }
    entity
  }

  def maxId = {
    if (repository.size > 0) {
      repository.keySet.max
    }
    else {
      0L
    }
  }
}
