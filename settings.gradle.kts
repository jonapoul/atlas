rootProject.name = "atlas"

apply("gradle/repositories.gradle.kts")

pluginManagement {
  includeBuild("build-logic")
}

include(
  ":atlas-core",
  ":atlas-d2",
  ":atlas-graphviz",
  ":atlas-mermaid",
  ":atlas-test",
)
