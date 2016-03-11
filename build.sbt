organization := "com.evojam"
name := "mongo-drivers-benchmarks"

version := "0.1.0"
scalaVersion := "2.11.7"

scalaSource in Compile := baseDirectory.value / "src"
scalaSource in Test := baseDirectory.value / "test"

testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")
logBuffered := false
parallelExecution in Test := false


resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/snapshots"

resolvers += "Sonatype OSS Releases" at
  "https://oss.sonatype.org/content/repositories/releases"

libraryDependencies ++= Seq(
  "com.storm-enroute" %% "scalameter" % "0.7",
  "org.mongodb" % "mongodb-driver-async" % "3.2.2",
  "org.reactivemongo" %% "reactivemongo" % "0.11.10",
  "org.slf4j" % "slf4j-log4j12" % "1.7.18"

)
  
