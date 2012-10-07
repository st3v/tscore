name := "tscore"
 
organization := "org.kiva"
 
version := "1.0.0=SNAPSHOT"
 
scalaVersion := "2.9.2"
 
libraryDependencies += "org.scalatest" %% "scalatest" % "1.7.2" % "test"

libraryDependencies += "org.springframework.data" % "spring-data-neo4j" % "2.0.2.RELEASE" excludeAll(
                                                                                              ExclusionRule(organization = "com.sun.jdmk"),
                                                                                              ExclusionRule(organization = "com.sun.jmx"),
                                                                                              ExclusionRule(organization = "javax.jms")
                                                                                            )
 
resolvers += "Spring Staging Repository" at "https://repo.springsource.org/libs-staging-local"

resolvers += "Spring Milestone Repository" at "http://repo.springsource.org/libs-milestone"

resolvers += "Spring Snapshot Repository" at "https://repo.springsource.org/libs-snapshot"

resolvers += "Spring Release Repository" at "https://repo.springsource.org/libs-release"

parallelExecution in Test := false
