package org.tscore.trust.model

class Subject extends org.tscore.graph.model.Subject {
  var name: String = null
  var description: String = null
}

object Subject {
  def apply(id: Long = 0,
            name: String,
            description: String = null) = {
    val subject = new Subject()
    if (id>0) subject.id = id
    subject.name = name
    subject.description = Option(description).getOrElse("")
    subject
  }
}
