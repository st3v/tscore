package org.tscore.web.lib.service

class ServiceMock[T] (implicit val m: Manifest[T]) {
  var resourceManifest = m
  var repository: List[T] = Nil
  var listeners: List[T => Unit] = Nil
}
