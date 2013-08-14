import sbt._
import Keys._
import Process._
import sbtassembly.Plugin._
import AssemblyKeys._

object MiniboxingBuild extends Build {

  val scalaVer = "2.10.3-SNAPSHOT"

  val defaults = Defaults.defaultSettings ++ Seq(
    scalaVersion := scalaVer,
    scalaBinaryVersion := "2.10",
    scalaSource in Compile <<= baseDirectory(_ / "src"),
    javaSource in Compile <<= baseDirectory(_ / "src"),
    scalaSource in Test <<= baseDirectory(_ / "test"),
    javaSource in Test <<= baseDirectory(_ / "test"),
    resourceDirectory in Compile <<= baseDirectory(_ / "resources"),
    compileOrder := CompileOrder.JavaThenScala,

    unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_)),
    unmanagedSourceDirectories in Test <<= (scalaSource in Test)(Seq(_)),
    //http://stackoverflow.com/questions/10472840/how-to-attach-sources-to-sbt-managed-dependencies-in-scala-ide#answer-11683728
    com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys.withSource := true,

    libraryDependencies ++= Seq(
      "org.scalacheck" %% "scalacheck" % "1.10.0" % "test",
      "com.novocode" % "junit-interface" % "0.10-M2" % "test"
    ),

    parallelExecution in Test := false,
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v"),

    resolvers in ThisBuild ++= Seq(
      ScalaToolsSnapshots,
      "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
      "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
    )

    /* libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-library" % scalaVer,
      "org.scala-lang" % "scala-reflect" % scalaVer,
      "org.scala-lang" % "scala-compiler" % scalaVer,
      "org.scala-lang" % "scala-partest" % scalaVer,
      "com.googlecode.java-diff-utils" % "diffutils" % "1.2.1"
    ) */
  )

  val scalaMeter = {
    val sMeter  = Seq("com.github.axel22" %% "scalameter" % "0.3")
    // val sMeter  = Seq("com.github.axel22" % "scalameter_2.10.0" % "0.2.1-SNAPSHOT") // published locally
    Seq(
      libraryDependencies ++= sMeter, 
      testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")
    )
  }

  lazy val _macros     = Project(id = "macros",                base = file(".")) aggregate (scratchpad)
  lazy val scratchpad  = Project(id = "macros-scratchpad",     base = file("components/scratchpad"),     settings = defaults)
}
