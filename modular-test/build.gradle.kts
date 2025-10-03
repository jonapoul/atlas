plugins {
  id("modular.convention.plugin")
  id("modular.convention.test")
  alias(libs.plugins.buildConfig)
}

buildConfig {
  generateAtSync = true
  sourceSets.getByName("test") {
    packageName = "modular.test"
    useKotlinOutput { topLevelConstants = true }
    buildConfigField<String>("AGP_VERSION", libs.versions.agp.get())
    buildConfigField<String>("KOTLIN_VERSION", libs.versions.kotlin.get())
  }
}

dependencies {
  testImplementation(kotlin("stdlib"))
  testImplementation(project(":modular-core"))
  testImplementation(project(":modular-graphviz"))
  testImplementation(project(":modular-mermaid"))
}
