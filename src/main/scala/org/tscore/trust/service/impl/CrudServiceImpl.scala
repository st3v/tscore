package org.tscore.trust.service.impl

import org.springframework.transaction.annotation.Transactional
import org.springframework.data.neo4j.repository.GraphRepository
import org.tscore.trust.service.CrudService
import scala.collection.JavaConversions._

trait CrudServiceImpl[T <: {var id: java.lang.Long}] extends CrudService[T] {
  protected val repository: GraphRepository[T] = null

  def getAll = repository.findAll().as(classOf[java.util.List[T]])

  def find(id: Long): Option[T] = Option(repository.findOne(id))

  @Transactional
  def add(entity: T): Option[T] = {
    if (entity.id != null && !repository.exists(entity.id)) {
      entity.id = null
    }
    Option(repository.save(entity))
  }

  @Transactional
  def delete(id: Long): Option[T] = {
    find(id) match {
      case Some(entity) => {
        repository.delete(id)
        Some(entity)
      }
      case None => None
    }
  }

  def prependListener(f: (T) => Unit) {}
}
