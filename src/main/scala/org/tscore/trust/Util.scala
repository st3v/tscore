package org.tscore.trust

object Util {
  def safeCast[T : Manifest](instance: AnyRef) : T = {
    if (Manifest.singleType(instance) <:< manifest[T]) {
      instance.asInstanceOf[T]
    }
    else {
      null.asInstanceOf[T]
    }
  }
}
