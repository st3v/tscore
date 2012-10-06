import sbt._
import Keys._
 
object ProjectBuild extends Build {
    lazy val root = Project(id = "tscore",
                            base = file("."))
}
