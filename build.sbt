ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "2.13.5"

val CatsEffectVersion = "3.5.3"

lazy val root = (project in file(".")).settings(
  name := "cats-effect-and-fs2-training",
  libraryDependencies ++= Seq(
    // "core" module - IO, IOApp, schedulers
    // This pulls in the kernel and std modules automatically.
    "org.typelevel" %% "cats-effect" % CatsEffectVersion,
    // concurrency abstractions and primitives (Concurrent, Sync, Async etc.)
    "org.typelevel" %% "cats-effect-kernel" % CatsEffectVersion,
    // standard "effect" library (Queues, Console, Random etc.)
    "org.typelevel" %% "cats-effect-std" % CatsEffectVersion,
    // better monadic for compiler plugin as suggested by documentation
    compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    "org.typelevel" %% "munit-cats-effect-3" % "1.0.7" % Test
  )
)


//Amonite
libraryDependencies += {
  val version = scalaBinaryVersion.value match {
    case "2.10" => "1.0.3"
    case "2.11" => "1.6.7"
    case _ ⇒ "3.0.0-M1"
  }
  "com.lihaoyi" % "ammonite" % version % "test" cross CrossVersion.full
}

sourceGenerators in Test += Def.task {
  val file = (sourceManaged in Test).value / "amm.scala"
  IO.write(file, """object amm extends App { ammonite.AmmoniteMain.main(args) }""")
  Seq(file)
}.taskValue

(fullClasspath in Test) ++= {
  (updateClassifiers in Test).value
    .configurations
    .find(_.configuration.name == Test.name)
    .get
    .modules
    .flatMap(_.artifacts)
    .collect{case (a, f) if a.classifier == Some("sources") => f}
}
