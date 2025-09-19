rootProject.name = "modular"

apply("gradle/repositories.gradle.kts")

pluginManagement {
  includeBuild("build-logic")
}

include(
  "modular-temp",
)
