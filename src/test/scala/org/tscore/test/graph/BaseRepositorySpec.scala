package org.tscore.test.graph

import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FunSuite}
import org.springframework.context.support.GenericXmlApplicationContext
import org.springframework.data.neo4j.repository.GraphRepository

/**
 * Base class for test suites that use Neo4j repositories.
 *
 * @tparam T  Type of NodeEntity for the Neo4j repository.
 */
abstract class BaseRepositorySpec[T] extends FunSuite with BeforeAndAfterAll with BeforeAndAfter {
  var ctx: GenericXmlApplicationContext = null

  /**
   * Open application context before first test.
   */
  override def beforeAll() {
    ctx = new GenericXmlApplicationContext("classpath*:/META-INF/spring/module-context.xml")
  }

  /**
   * Close application context after last test.
   */
  override def afterAll() {
    ctx.close()
  }

  /**
   * Cleanup repository before each test.
   */
  before {
    getRepository.deleteAll()
  }

  /**
   * Return type-specific repository
   *
   * @return  GraphRepository for type T.
   */
  def getRepository : GraphRepository[T]
}

