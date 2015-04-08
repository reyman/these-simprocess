#!/usr/bin/env scalas

/***
scalaVersion := "2.11.5"

resolvers += Resolver.url("Sonatype OSS Snapshots",url("https://oss.sonatype.org/content/repositories/snapshots"))(Resolver.ivyStylePatterns)

libraryDependencies += "com.lihaoyi" %% "ammonite" % "0.1.4"

*/

import ammonite.all._
import ammonite.pprint._
import Config.Defaults._
import ammonite.ops.%
import scala.sys.process._

//val wd = processWorkingDir

val wd = root/'home/'srey/'TRAVAUX/'THESE/'REPOSITORY_GIT/"simprocess-these"/'Schema_originaux
val destination = root/'home/'srey/'TRAVAUX/'THESE/'REPOSITORY_GIT/"simprocess-these"/'FigurePartie2

val results = (ls! wd |? (_.ext == "svg"))

results.map{ r => Seq("sh","-c", "inkscape -z -D --file="+r+" --export-pdf="+r.toString.dropRight(4)+".pdf").!!}

//val pdf = (ls! wd |? (_.ext == "pdf")) | cp.all


