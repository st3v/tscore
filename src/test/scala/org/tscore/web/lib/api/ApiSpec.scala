package org.tscore.web.lib.api

import org.tscore.web.WebSpec2
import org.springframework.context.support.GenericXmlApplicationContext
import org.tscore.web.lib.service.SubjectServiceMock

class ApiSpec extends WebSpec2(new bootstrap.liftweb.Boot().boot _) with Context {
  override def is = args(sequential = true) ^ super.is
}

trait Context extends org.specs2.mutable.Before {
  private val ctx = new GenericXmlApplicationContext("classpath*:/META-INF/spring/module-context-test-web.xml")
  protected var subjectService: SubjectServiceMock = ctx.getBean(classOf[SubjectServiceMock])
  def before() {
    subjectService.subjects = Nil
  }
}
