package org.tscore.graph

import org.springframework.data.neo4j.annotation.{GraphId, NodeEntity}

@NodeEntity
class Subject {
  @GraphId
  var id: java.lang.Long = _

  override def toString = {
    "Subject %d".format(id)
  }
}
