package org.tscore.trust.model

sealed class Subject extends org.tscore.graph.model.Subject {
  var name: String = null
  var description: String = null

  override def equals(obj:Any) : Boolean = {
    super.equals(obj) &&
      obj.asInstanceOf[Subject].name == this.name &&
      obj.asInstanceOf[Subject].description == this.description
  }
}

object Subject {
  def apply(id: java.lang.Long = null,
            name: String,
            description: String = null) = {
    var subject: Subject = null
    if (Option(name).isDefined) {
      subject = new Subject()
      subject.id = Option(id).getOrElse(null)
      subject.name = name
      subject.description = Option(description).getOrElse("")
    }
    subject
  }
}
