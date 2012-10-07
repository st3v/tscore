package org.tscore.graph.model

class Actor extends Subject {
  var name : String = "Anonymous"

  override def equals(obj:Any) = {
    super.equals(obj) &&
      obj.isInstanceOf[Actor] &&
      obj.asInstanceOf[Actor].name == this.name
  }

  override def toString = {
    "Actor %d: \"%s\"".format(id, name)
  }
}