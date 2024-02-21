ThisBuild / scalaVersion     := "2.13.12"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "Basic WebSocket Server",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % "10.2.4",
      "com.typesafe.akka" %% "akka-stream" % "2.6.14",
      "org.scalameta" %% "munit" % "0.7.29" % Test // Replace with the actual version you need
    )
  )
