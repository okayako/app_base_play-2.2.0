name := "app_base"

version := "0.7"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "ws.securesocial" %% "securesocial" % "2.1.3",
  "be.objectify" %% "deadbolt-java" % "2.2-RC4",
  "be.objectify" %% "deadbolt-scala" % "2.2-RC2",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2",
  "com.novus" %% "salat" % "1.9.5",
  "org.scala-tools.time" % "time_2.9.1" % "0.5"
)     

resolvers ++= Seq(
  Resolver.url("sbt-plugin-releases", new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))(Resolver.ivyStylePatterns),
  Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns),
  Resolver.url("Objectify Play Snapshot Repository", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns)
)

play.Project.playScalaSettings
