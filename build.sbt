name := "tscore"
 
organization := "org.kiva"
 
version := "1.0.0=SNAPSHOT"
 
scalaVersion := "2.9.2"
 
libraryDependencies += "org.scalatest" %% "scalatest" % "1.7.2" % "test"
 
resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"
