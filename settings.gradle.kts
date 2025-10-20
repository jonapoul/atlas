rootProject.name = "atlas"

apply("gradle/repositories.gradle.kts")

pluginManagement {
  includeBuild("build-logic")
}

plugins {
  id("com.fueledbycaffeine.fastsync") version "0.3.0"
}

include(
  ":atlas-core",
  ":atlas-d2",
  ":atlas-graphviz",
  ":atlas-mermaid",
  ":atlas-test",
)
