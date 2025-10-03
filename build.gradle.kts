plugins {
  alias(libs.plugins.buildConfig) apply false
  alias(libs.plugins.detekt) apply false
  alias(libs.plugins.dokka) apply false
  alias(libs.plugins.kotlinJvm) apply false
  alias(libs.plugins.licensee) apply false
  alias(libs.plugins.publish) apply false

  alias(libs.plugins.publishReport)
  alias(libs.plugins.spotless)
  id("modular.convention.spotless")
}
