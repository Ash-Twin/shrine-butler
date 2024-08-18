ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "shrine-butler",
    idePackagePrefix := Some("shrine.butler"),
    libraryDependencies ++=Seq(
      "io.github.ollama4j" % "ollama4j" % "1.0.82",
      "com.lihaoyi" %% "cask" % "0.9.4",
      "com.lihaoyi" %% "requests" % "0.9.0",
      "com.lihaoyi" %% "os-lib" % "0.10.3",
      "com.meilisearch.sdk" % "meilisearch-java" % "0.13.0"
    )
  )
