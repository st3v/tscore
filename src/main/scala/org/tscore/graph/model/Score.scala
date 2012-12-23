package org.tscore.graph.model

import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.ConverterFactory
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.annotate.JsonMethod
import org.tscore.graph.util.Json

/**
 * Score trait represents scores on Endorsements and Actors.
 * Sub-classes must either be case classes or provide a parameter-less constructor (i.e. def this())
 */
class Score {}

/**
 * Converter transforms an instance of class Score to a JSON string that can be stored in Neo4J.
 */
class StringToScoreConverter extends ConverterFactory[String, Score] {
  def getConverter[T <: Score](targetType: Class[T]) = {
    new StringToScoreConverter[T]()
  }

  class StringToScoreConverter[T <: Score] extends Converter[String, T] {
    def convert(source: String): T = {
      Json.parse(source, classOf[Score]).asInstanceOf[T]
    }
  }
}

/**
 * Converter transforms JSON string to an instance of class Score or one of its subclasses.
 */
class ScoreToStringConverter extends ConverterFactory[Score, String] {
  def getConverter[T <: String](targetType: Class[T]) = {
    new ScoreToStringConverter[T]
  }

  class ScoreToStringConverter[T <: String] extends Converter[Score, T] {
    def convert(source: Score) = {
      Json.generate(source).asInstanceOf[T]
    }
  }
}





