import org.scalafmt.sbt.ScalaFmtPlugin.autoImport._
import org.scalafmt.sbt.ScalaFmtPlugin.autoImport._

name := """akkapersistenceexample"""

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.11" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test")




val akkaVersion = "2.4.7"





resolvers += "dnvriend at bintray" at "http://dl.bintray.com/dnvriend/maven"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.5" % Test,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.github.dnvriend" %% "akka-persistence-inmemory" % "1.3.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.akka" %% "akka-persistence-query-experimental" % akkaVersion
)

scalafmtConfig := Some(file(".scalafmt"))

reformatOnCompileSettings

fork in run := true