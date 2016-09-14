resolvers in ThisBuild ++= Seq("Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
  "Artima Maven Repository" at "http://repo.artima.com/releases",
  Resolver.mavenLocal)

name := "focus"

version := "0.1-SNAPSHOT"

organization := "ru.wobot"

scalaVersion in ThisBuild := "2.11.8"

val flinkVersion = "1.1.2"
val scalatestVersion = "3.0.0"
val scalacticVersion = "3.0.0"
val mockitoVersion = "2.0.2-beta"

val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided",
  "org.scalactic" %% "scalactic" % scalacticVersion,
  "org.scalatest" %% "scalatest" % scalatestVersion % "test",
  "org.mockito" % "mockito-all" % mockitoVersion % "test"
)

lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= flinkDependencies
  )

mainClass in assembly := Some("ru.wobot.Job")

// make run command include the provided dependencies
run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))

// exclude Scala library from assembly
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)