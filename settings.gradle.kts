rootProject.name = "modular"

apply("gradle/repositories.gradle.kts")

pluginManagement {
  includeBuild("build-logic")
}

plugins {
  id("com.fueledbycaffeine.fastsync") version "0.3.0"
}

include(
  ":modular-core",
  ":modular-d2",
  ":modular-graphviz",
  ":modular-mermaid",
  ":modular-test",
)
