import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.zoneent",
      scalaVersion := "2.12.8",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "scala-snippets",
    libraryDependencies += apacheCommonsMath,
    libraryDependencies += "com.github.oshi" % "oshi-core" % "3.4.4",
    libraryDependencies += scalaTest % Test
  )
