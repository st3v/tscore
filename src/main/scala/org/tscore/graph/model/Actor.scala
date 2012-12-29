package org.tscore.graph.model

import org.springframework.data.neo4j.annotation.{RelatedToVia, Fetch, Indexed}
import org.springframework.data.neo4j.support.index.IndexType
import org.neo4j.graphdb.Direction
import scala.collection.JavaConversions._
import collection.Iterable

/**
 * Class actor represents an entity that can give and receive endorsements.
 */
class Actor extends Subject {
  /**
   * PRIVATE. Holds all outgoing endorsements for this actor.
   */
  @Fetch
  @RelatedToVia(`type` = "ENDORSES", direction = Direction.OUTGOING)
  private val outgoing : java.util.Set[Endorsement] = new java.util.HashSet[Endorsement]()

  /**
   * Returns all subjects that have been endorsed by the actor.
   *
   * @return
   */
  def endorsed : Iterable[Subject] = outgoing.map((e: Endorsement) => e.subject)

  /**
   * Returns all endorsements given by this actor.
   *
   * @return
   */
  def givenEndorsements : Iterable[Endorsement] = iterableAsScalaIterable(outgoing)

  /**
   * Creates an endorsement from this actor for the given subject with the given score.
   *
   * @param subject The subject to be endorsed by this actor.
   * @return
   */
  def endorse(subject: Subject) : Endorsement = {
    val endorsement = Endorsement(this, subject)
    outgoing.add(endorsement)
    subject.incoming.add(endorsement)
    endorsement
  }

  override def equals(obj:Any) : Boolean = {
    super.equals(obj) &&
      obj.isInstanceOf[Actor] &&
      obj.asInstanceOf[Actor].id == this.id
  }
}