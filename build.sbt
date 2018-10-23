name := """scala-play-slick"""
organization := "com.vauldex"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  guice,
  "com.typesafe.akka"      %%  "akka-actor"               % "2.5.17",
  "com.typesafe.play"      %%  "play-json"                % "2.7.0-M1",
  "com.typesafe.slick"     %%  "slick"                    % "3.2.1",
  "com.typesafe.play"      %%  "play-slick"               % "3.0.3",
  "com.typesafe.play"      %%  "play-slick-evolutions"    % "3.0.3",
  "com.github.tminglei"    %%  "slick-pg"                 % "0.15.2",
  "org.scalatestplus.play" %%  "scalatestplus-play"       % "3.1.2"       % Test
)
