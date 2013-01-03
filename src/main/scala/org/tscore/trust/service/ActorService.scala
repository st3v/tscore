package org.tscore.trust.service

import org.tscore.trust.model.Actor
import org.tscore.trust.model.score.ActorScore

trait ActorService extends CrudService[Actor] with SearchService[Actor] {
  def create(id: Option[java.lang.Long], name: String, description: Option[String], score: Option[ActorScore]): Option[Actor]
  def find(name: String): Option[Actor]
}
