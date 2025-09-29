@file:Suppress("UnstableApiUsage")

rootProject.name = "build-logic"

apply(from = "../gradle/repositories.gradle.kts")

dependencyResolutionManagement {
  repositories {
    gradlePluginPortal()
  }

  versionCatalogs {
    create("libs") {
      from(files("../gradle/libs.versions.toml"))
    }
  }
}
