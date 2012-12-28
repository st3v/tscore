package org.tscore.trust.service.impl

import org.tscore.trust.service.EndorsementServiceTrait
import org.tscore.trust.model.Endorsement
import scala.collection.JavaConversions._
import org.springframework.beans.factory.annotation.Autowired
import org.tscore.trust.repository.EndorsementRepository

class EndorsementService extends EndorsementServiceTrait {
  @Autowired
  private val repository: EndorsementRepository = null

  def getAllEndorsements = repository.findAll().as(classOf[java.util.List[Endorsement]])

  def findEndorsementById(endorsementId: Long) = null

  def searchEndorsementsByKeyword(keyword: String) = null

  def addEndorsement(endorsement: Endorsement) = null

  def deleteEndorsement(endorsementId: String) {}

  def prependEndorsementListener(f: (Endorsement) => Unit) {}
}
