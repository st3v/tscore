package org.tscore.graph.model

import org.springframework.core.convert.converter.GenericConverter
import org.springframework.core.convert.TypeDescriptor

class EndorsementScoreToStringConverter extends GenericConverter {
  def convert(source: Object, sourceType: TypeDescriptor, targetType: TypeDescriptor): Object = {
    source.toString
  }

  def getConvertibleTypes: java.util.Set[GenericConverter.ConvertiblePair] = {
    scala.collection.JavaConversions.setAsJavaSet(Set(new GenericConverter.ConvertiblePair(classOf[EndorsementScore[_]], classOf[String])))
  }
}

class StringToEndorsementScoreConverter extends GenericConverter {
  def convert(source: Object, sourceType: TypeDescriptor, targetType: TypeDescriptor): Object = {
//    val parts = source.toString.split("-")
//    val score_type = parts(0)
//    val score_value = parts(1)
//    val cls = Class.forName(score_type)

    new NumericEndorsementScore(source.toString.toDouble)
  }

  def getConvertibleTypes: java.util.Set[GenericConverter.ConvertiblePair] = {
    scala.collection.JavaConversions.setAsJavaSet(Set(new GenericConverter.ConvertiblePair(classOf[String], classOf[EndorsementScore[_]])))
  }
}