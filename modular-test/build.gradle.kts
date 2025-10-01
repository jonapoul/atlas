import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

plugins {
  id("modular.convention.kotlin")
  alias(libs.plugins.buildConfig)
}

buildConfig {
  packageName = "modular.test"
  generateAtSync = true
  useKotlinOutput { topLevelConstants = true }
  buildConfigField<String>("AGP_VERSION", libs.versions.agp.get())
  buildConfigField<String>("KOTLIN_VERSION", libs.versions.kotlin.get())
}

dependencies {
  api(gradleTestKit())
  api(kotlin("stdlib"))
  api(kotlin("test"))
  api(libs.assertk)
  api(libs.junit.api)
  implementation(project(":modular-core"))
}

@OptIn(ExperimentalAbiValidation::class)
kotlin {
  abiValidation.enabled = false
}
