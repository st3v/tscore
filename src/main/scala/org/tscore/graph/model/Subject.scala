package org.tscore.graph.model

import org.springframework.data.neo4j.annotation._
import org.neo4j.graphdb.Direction
import scala.collection.JavaConversions._
import collection.Iterable

@NodeEntity
class Subject {
  @GraphId
  var id: java.lang.Long = _

  /**
   * PRIVATE. Holds all incoming endorsements for this subject.
   */
  @RelatedToVia(`type` = "ENDORSES", direction = Direction.INCOMING)
  protected[model] val incoming : java.util.Set[Endorsement] = new java.util.HashSet[Endorsement]()

  /**
   * Returns all actors that have endorsed this subject.
   *
   * @return
   */
  def endorsers : Iterable[Actor] = incoming.map((e: Endorsement) => e.actor)

  /**
   * Returns all endorsements that have been given for this subjects.
   *
   * @return
   */
  def receivedEndorsements : Iterable[Endorsement] = iterableAsScalaIterable(incoming)

  override def equals(obj:Any) : Boolean = {
    obj.isInstanceOf[Subject] && obj.asInstanceOf[Subject].id == this.id
  }

  override def toString : String = "%s:%s".format(this.getClass.getSimpleName, id)
}
