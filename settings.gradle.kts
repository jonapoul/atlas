@file:Suppress("UnstableApiUsage", "AvoidApplyPluginMethod")

rootProject.name = "atlas"

apply("gradle/repositories.gradle.kts")

pluginManagement {
  repositories {
    google {
      mavenContent {
        includeGroupByRegex(".*android.*")
        includeGroupByRegex(".*google.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}
