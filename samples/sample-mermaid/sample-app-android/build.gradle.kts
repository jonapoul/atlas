plugins {
  alias(libs.plugins.agp.app)
  alias(libs.plugins.kotlin.android)
  id("dev.jonpoulton.modular.leaf")
}

android {
  namespace = "dev.jonpoulton.modular.sample.agp.app"
  compileSdk = 36
}

dependencies {
  implementation(project(":sample-lib-android"))
  implementation(project(":sample-lib-kotlin-mp"))
}
