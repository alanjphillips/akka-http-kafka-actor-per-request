enablePlugins(JavaAppPackaging)

name := "akka-http-kafka-actor-per-request"

version := "1.0"

scalaVersion := "2.11.8"

val akkaVersion = "2.4.11"

resolvers ++= Seq(
  Resolver.bintrayRepo("hseeberger", "maven")
)

libraryDependencies ++= Seq(
  "de.heikoseeberger"         %% "akka-http-circe"        % "1.10.0",
  "io.circe"                  %% "circe-core"             % "0.4.1",
  "io.circe"                  %% "circe-generic"          % "0.4.1",
  "io.circe"                  %% "circe-parser"           % "0.4.1",
  "com.typesafe.akka"         %% "akka-stream-kafka"      % "0.13",
  "com.typesafe.akka"         %% "akka-http-experimental" % akkaVersion
)