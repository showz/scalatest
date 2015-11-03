name := "play-scala"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

//  jdbc,
libraryDependencies ++= Seq(
  cache,
  ws,
  specs2 % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

// slick
libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.37",
  "com.typesafe.slick" %% "slick" % "3.1.0",
  "c3p0" % "c3p0" % "0.9.1.2",
  "com.typesafe.play" %% "play-slick" % "1.1.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "1.1.0",
  "org.sangria-graphql" %% "sangria" % "0.4.3"
)