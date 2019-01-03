name := "skeeper"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
  "commons-cli" % "commons-cli" % "1.4",
  "org.apache.maven.wagon" % "wagon-http" % "3.0.0",
  "org.apache.maven" % "maven-ant-tasks" % "2.1.3",
  "io.netty" % "netty-all" % "4.1.28.Final",
  "org.jline" % "jline" % "3.6.2",
  "org.apache.yetus" % "audience-annotations" % "0.8.0",
  "org.apache.commons" % "commons-collections4" % "4.2",
  "xerces" % "xercesImpl" % "2.12.0",
  "com.beachape" %% "enumeratum" % "1.5.13",
  "com.typesafe" % "config" % "1.3.2",
  "com.jsuereth" %% "scala-arm" % "2.0",
  "org.apache.avro" % "avro" % "1.8.2",
  "com.opencsv" % "opencsv" % "4.2",
  "org.scalacheck" %% "scalacheck" % "1.14.0" % Test

)
