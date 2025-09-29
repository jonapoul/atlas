plugins {
  alias(libs.plugins.detekt) apply false
  alias(libs.plugins.dokka) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.licensee) apply false
  alias(libs.plugins.publish) apply false

  alias(libs.plugins.spotless)
  id("modular.convention.spotless")
}
