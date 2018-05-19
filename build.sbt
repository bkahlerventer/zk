name := "zk"

version := "0.1"

scalaVersion := "2.12.5"


libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
  "commons-cli" % "commons-cli" % "1.4",
  "org.apache.maven.wagon" % "wagon-http" % "3.0.0",
  "io.netty" % "netty-all" % "4.1.23.Final",
  "org.jline" % "jline" % "3.6.2",
  "xerces" % "xercesImpl" % "2.11.0"
)