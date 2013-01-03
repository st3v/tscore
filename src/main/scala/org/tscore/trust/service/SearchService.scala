package org.tscore.trust.service

trait SearchService[T] {
  def search(keyword: String): Seq[T]
}
