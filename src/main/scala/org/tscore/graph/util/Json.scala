package org.tscore.graph.util

import org.codehaus.jackson.map.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.codehaus.jackson.annotate.JsonMethod
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility


trait Json {
  private val mapper = new ObjectMapper()

  mapper.registerModule(DefaultScalaModule)
  mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)
  mapper.setVisibility(JsonMethod.ALL, Visibility.ANY)

  def generate[T](source: T) : String = {
    mapper.writeValueAsString(source)
  }

  def parse[T](source: String, clazz: Class[T]): T = {
    mapper.readValue(source, clazz)
  }
}

object Json extends Json