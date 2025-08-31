rootProject.name = "modular-sample"

apply("../gradle/repositories.gradle.kts")

includeBuild("..")

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../gradle/libs.versions.toml"))
    }
  }
}

include(
  "sample-app-android",
  "sample-lib-android",
  "sample-lib-java",
  "sample-lib-kotlin-jvm",
  "sample-lib-kotlin-mp",
)
