package org.tscore.trust.model.score

import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.ConverterFactory
import org.tscore.graph.util.Json

/**
 * TrustScore trait represents scores on Endorsements and Actors.
 * Sub-classes must either be case classes or provide a parameter-less constructor (i.e. def this())
 */
trait TrustScore {}

/**
 * Converter transforms an instance of class Score to a JSON string that can be stored in Neo4J.
 */
class StringToTrustScoreConverter extends ConverterFactory[String, TrustScore] {
  def getConverter[T <: TrustScore](targetType: Class[T]) = {
    new StringToScoreConverter[T]()
  }

  class StringToScoreConverter[T <: TrustScore] extends Converter[String, T] {
    def convert(source: String): T = {
      Json.parse(source, classOf[TrustScore]).asInstanceOf[T]
    }
  }
}

/**
 * Converter transforms JSON string to an instance of class TrustScore or one of its subclasses.
 */
class TrustScoreToStringConverter extends ConverterFactory[TrustScore, String] {
  def getConverter[T <: String](targetType: Class[T]) = {
    new ScoreToStringConverter[T]
  }

  class ScoreToStringConverter[T <: String] extends Converter[TrustScore, T] {
    def convert(source: TrustScore) = {
      Json.generate(source).asInstanceOf[T]
    }
  }
}





