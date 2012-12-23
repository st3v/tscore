package org.tscore.graph.model

import org.springframework.data.neo4j.annotation.{RelatedToVia, Fetch, RelatedTo, Indexed}
import org.springframework.data.neo4j.support.index.IndexType
import org.neo4j.graphdb.Direction
import scala.collection.JavaConversions._
import collection.Iterable

/**
 * Class actor represents an entity that can give and receive endorsements.
 */
class Actor extends Subject {
  /**
   * Name for this actor.
   */
  @Indexed(indexName = "name", indexType = IndexType.FULLTEXT, unique=true)
  var name : String = "Anonymous"

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
   * @param score   The score for the endorsement.
   * @return
   */
  def endorse(subject: Subject, score: Score) : Endorsement = {
    val endorsement = Endorsement(this, subject, score)
    outgoing.add(endorsement)
    subject.incoming.add(endorsement)
    endorsement
  }

  override def toString : String = "%s (name: %s)".format(super.toString, name)

  override def equals(obj:Any) : Boolean = {
    super.equals(obj) &&
      obj.isInstanceOf[Actor] &&
      obj.asInstanceOf[Actor].name == this.name
  }
}

/**
 * Companion object that provides a convenience constructor.
 */
object Actor {
  def apply(name: String) = {
    val actor = new Actor()
    actor.name = name
    actor
  }
}