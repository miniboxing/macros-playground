import sbt._
import Keys._
import Process._
// import sbtassembly.Plugin._
// import AssemblyKeys._

object MiniboxingBuild extends Build {

  val scalaVer = "2.10.4"

  val defaults = Defaults.defaultSettings ++ Seq(
    scalaVersion := scalaVer,
    scalaSource in Compile := baseDirectory.value / "src",
    javaSource in Compile := baseDirectory.value / "src",
    scalaSource in Test := baseDirectory.value / "test",
    javaSource in Test := baseDirectory.value / "test",
    resourceDirectory in Compile := baseDirectory.value / "resources",
    compileOrder := CompileOrder.JavaThenScala,

    unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value),
    unmanagedSourceDirectories in Test := Seq((scalaSource in Test).value),
    //http://stackoverflow.com/questions/10472840/how-to-attach-sources-to-sbt-managed-dependencies-in-scala-ide#answer-11683728
    com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys.withSource := true,

    resolvers in ThisBuild ++= Seq(
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots")
    ),

    libraryDependencies ++= Seq(
      // "org.scalacheck" %% "scalacheck" % "1.10.0" % "test",
      "com.novocode" % "junit-interface" % "0.10-M2" % "test"
    ),

    scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked", "-Xlint"),

    publishArtifact in packageDoc := false,

    parallelExecution in Test := false,
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v"),

    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-library" % scalaVer,
      "org.scala-lang" % "scala-reflect" % scalaVer,
      "org.scala-lang" % "scala-compiler" % scalaVer,
      // "org.scala-lang" % "scala-partest" % scalaVer,
      "com.googlecode.java-diff-utils" % "diffutils" % "1.2.1"
    )
  )

  val scalaMeter = {
    val sMeter  = Seq("com.github.axel22" %% "scalameter" % "0.3")
    // val sMeter  = Seq("com.github.axel22" % "scalameter_2.10.0" % "0.2.1-SNAPSHOT") // published locally
    Seq(
      libraryDependencies ++= sMeter, 
      testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")
    )
  }

  lazy val _macros     = Project(id = "macros",                 base = file(".")) aggregate (scratchpad)
  lazy val definition  = Project(id = "macros-play-definition", base = file("components/definition"), settings = defaults)
  lazy val scratchpad  = Project(id = "macros-play-scratchpad", base = file("components/scratchpad"), settings = defaults) dependsOn (definition)
}
