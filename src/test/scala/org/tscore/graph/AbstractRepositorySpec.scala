package org.tscore.graph

import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FunSuite}
import org.springframework.context.support.GenericXmlApplicationContext
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.transaction.annotation.Transactional
import org.tscore.TestSuite

/**
 * Base class for test suites that use Neo4j repositories.
 *
 */
@Transactional
abstract class AbstractRepositorySpec extends TestSuite with BeforeAndAfterAll with BeforeAndAfter {
  var ctx: GenericXmlApplicationContext = null

  /**
   * Open application context before first test.
   */
  override def beforeAll() {
    ctx = new GenericXmlApplicationContext("classpath*:/META-INF/spring/module-context-test-graph.xml")
  }

  /**
   * Close application context after last test.
   */
  override def afterAll() {
    ctx.close()
  }

  /**
   * Cleanup all used repositories before each test.
   */
  before {
    getRepositories.foreach(
      repo => repo.deleteAll()
    )
  }

  /**
   * Array that holds the classes of all graph repositories used in the test.
   * Must be override by child classes.
   *
   * @return
   */
  def repositories : Array[Class[_ <: GraphRepository[_]]]

  /**
   * Returns instances for all graph repositories used in the tes.
   *
   * @return  Array of GraphRepositories.
   */
  def getRepositories : Array[_ <: GraphRepository[_]] = repositories.map(getRepository(_))

  /**
   * Returns an instance for a given graph repository class.  That instance is retrieved from
   * the ApplicationContext and then put into the repository cache.  Once the repository
   * for a given class has been cached subsequent calls of this method will return the cached
   * instance instead of using the ApplicationContext again.
   *
   * @param clazz
   * @return
   */
  def getRepository[T](clazz: Class[T]) = {
    ctx.getBean(clazz)
  }
}



