name := "tscore"
 
organization := "org.kiva"
 
version := "1.0.0=SNAPSHOT"
 
scalaVersion := "2.9.2"
 
libraryDependencies += "org.scalatest" %% "scalatest" % "1.7.2" % "test"

libraryDependencies += "org.springframework.data" % "spring-data-neo4j-rest" % "2.1.0.RELEASE" excludeAll(
                                                                                              ExclusionRule(organization = "com.sun.jdmk"),
                                                                                              ExclusionRule(organization = "com.sun.jmx"),
                                                                                              ExclusionRule(organization = "javax.jms")
                                                                                            )

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.6.6"

resolvers += "Spring Staging Repository" at "https://repo.springsource.org/libs-staging-local"

resolvers += "Spring Milestone Repository" at "http://repo.springsource.org/libs-milestone"

resolvers += "Spring Snapshot Repository" at "https://repo.springsource.org/libs-snapshot"

resolvers += "Spring Release Repository" at "https://repo.springsource.org/libs-release"

resolvers += "Neo4j Releases" at "http://m2.neo4j.org/content/repositories/releases"

parallelExecution in Test := false
