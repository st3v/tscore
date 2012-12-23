package org.tscore.graph.util

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility


trait Json {
  private val mapper = new ObjectMapper()

  mapper.registerModule(DefaultScalaModule)
  mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL)
  mapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY)

  def generate[T](source: T) : String = {
    mapper.writeValueAsString(source)
  }

  def parse[T](source: String, clazz: Class[T]): T = {
    mapper.readValue(source, clazz)
  }
}

object Json extends Json