package org.tscore.graph.model

import org.springframework.data.neo4j.annotation.{GraphId, NodeEntity}

@NodeEntity
class Subject {
  @GraphId
  var id: java.lang.Long = _

  override def equals(obj:Any) = {
    obj.isInstanceOf[Subject] && obj.asInstanceOf[Subject].id == this.id
  }

  override def toString = {
    "Subject %d".format(id)
  }
}
