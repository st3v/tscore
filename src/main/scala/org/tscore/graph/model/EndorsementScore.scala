package org.tscore.graph.model

import org.springframework.core.convert.converter.GenericConverter
import org.springframework.core.convert.TypeDescriptor

trait EndorsementScore[T] {

  def +(that: T) : T

  def -(that: T) : T

  def equiv(that: Any) : Boolean

  def <(that: T) : Boolean

  def <=(that: T) : Boolean

  def >(that: T) : Boolean

  def >=(that: T) : Boolean

  def isPositive: Boolean

  def isNegative: Boolean

  override def equals(obj : Any) : Boolean = this.equiv(obj)

  def toString : String

}

trait EndorsementScoreFactory[AnyRef] {
  def createScoreFromString(value: String):EndorsementScore[_]
}

class EndorsementScoreToStringConverter extends GenericConverter {
  def convert(source: Object, sourceType: TypeDescriptor, targetType: TypeDescriptor): Object = {
    source.getClass.getName + '-' + source.toString
  }

  def getConvertibleTypes: java.util.Set[GenericConverter.ConvertiblePair] = {
    scala.collection.JavaConversions.setAsJavaSet(Set(new GenericConverter.ConvertiblePair(classOf[EndorsementScore[_]], classOf[String])))
  }
}

class StringToEndorsementScoreConverter extends GenericConverter {
  def convert(source: Object, sourceType: TypeDescriptor, targetType: TypeDescriptor): Object = {
    val parts = source.toString.split("-")
    val className = parts(0) + "Factory"
    Class.forName(className).newInstance().asInstanceOf[{ def createScoreFromString(value: String): EndorsementScore[_] }].createScoreFromString(parts(1))
  }

  def getConvertibleTypes: java.util.Set[GenericConverter.ConvertiblePair] = {
    scala.collection.JavaConversions.setAsJavaSet(Set(new GenericConverter.ConvertiblePair(classOf[String], classOf[EndorsementScore[_]])))
  }
}


