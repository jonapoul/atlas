@file:Suppress("UnstableApiUsage")

rootProject.name = "build-logic"

apply("../gradle/repositories.gradle.kts")

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
