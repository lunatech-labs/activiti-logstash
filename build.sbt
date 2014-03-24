name := "activiti-logstash"

scalaVersion := "2.10.3"

resolvers ++= Seq(
  "Alfresco Maven" at "https://maven.alfresco.com/nexus/content/groups/public/")

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.1",
  "com.h2database" % "h2" % "1.3.175",
  "org.activiti" % "activiti-engine" % "5.15")

resourceDirectory in Compile := file("resources")

scalaSource in Compile := file("src")

mainClass in (Compile, run) := Some("com.lunatech.activiti.Main")
