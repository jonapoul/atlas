rootProject.name = "sample-mermaid"

apply("../../gradle/repositories.gradle.kts")

includeBuild("../..")

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../../gradle/libs.versions.toml"))
    }
  }
}

include(
  "module-android:app",
  "module-android:lib",
  "module-kotlin:kmp",
  "module-kotlin:jvm",
  "module-java",
  "module-other",
)
