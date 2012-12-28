package org.tscore.trust.service

import org.tscore.trust.model.Endorsement

trait EndorsementServiceTrait {
  def getAllEndorsements: Seq[Endorsement]
  def findEndorsementById(endorsementId: Long): Endorsement
  def searchEndorsementsByKeyword(keyword: String): Seq[Endorsement]
  def addEndorsement(endorsement: Endorsement): Endorsement
  def deleteEndorsement(endorsementId: String)
  def prependEndorsementListener(f: Endorsement => Unit)
}
