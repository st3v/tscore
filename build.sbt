import com.typesafe.startscript.StartScriptPlugin

seq(StartScriptPlugin.startScriptForClassesSettings: _*)

name := "tscore"
 
organization := "org.kiva"
 
version := "1.0.0=SNAPSHOT"
 
scalaVersion := "2.9.2"

resolvers ++= Seq("Spring Staging Repository" at "https://repo.springsource.org/libs-staging-local",
                  "Spring Milestone Repository" at "http://repo.springsource.org/libs-milestone",
                  "Spring Snapshot Repository" at "https://repo.springsource.org/libs-snapshot",
                  "Spring Release Repository" at "https://repo.springsource.org/libs-release",
                  "Neo4j Releases" at "http://m2.neo4j.org/content/repositories/releases",
                  "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases" at "http://oss.sonatype.org/content/repositories/releases")

seq(com.github.siasia.WebPlugin.webSettings :_*)

scalacOptions ++= Seq("-deprecation", "-unchecked")

unmanagedResourceDirectories in Test <+= (baseDirectory) { _ / "src/main/webapp" }

libraryDependencies += "org.springframework.data" % "spring-data-neo4j-rest" % "2.1.0.RELEASE" excludeAll(ExclusionRule(organization = "com.sun.jdmk"),
                                                                                       ExclusionRule(organization = "com.sun.jmx"),
                                                                                       ExclusionRule(organization = "javax.jms"))

libraryDependencies ++= {
  val liftVersion = "2.5-M3"
  Seq(
    "org.springframework" % "spring-web" % "3.2.0.RELEASE",
    "org.scalatest"     %% "scalatest"          % "1.7.2"            % "test",
    "org.slf4j"         %  "slf4j-log4j12"      % "1.6.6",
    "net.liftweb"       %% "lift-webkit"        % liftVersion        % "compile",
    "net.liftweb"       %% "lift-testkit"       % liftVersion        % "test",
    "net.liftmodules"   %% "lift-jquery-module" % (liftVersion + "-2.0"),
    "org.eclipse.jetty" % "jetty-webapp"        % "8.1.7.v20120910"  % "container,test,compile",
    "org.eclipse.jetty" % "jetty-server"        % "8.1.7.v20120910"  % "container,test,compile",
    "org.eclipse.jetty" % "jetty-servlet"       % "8.1.7.v20120910"  % "container,test,compile",
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,test,compile" artifacts Artifact("javax.servlet", "jar", "jar"),
    "org.specs2"        %% "specs2"             % "1.12.1"           % "test",
    "com.fasterxml.jackson.core" % "jackson-core" % "2.1.1",
    "com.fasterxml.jackson.module" % "jackson-module-scala" % "2.1.2"
  )
}

parallelExecution in Test := false

