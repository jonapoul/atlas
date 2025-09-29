import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

plugins {
  id("modular.convention.kotlin")
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
