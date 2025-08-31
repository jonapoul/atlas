plugins {
  alias(libs.plugins.agp.lib)
  alias(libs.plugins.kotlin.android)
}

android {
  namespace = "dev.jonpoulton.modular.sample.agp.lib"
  compileSdk = 36
}

dependencies {
  implementation(project(":sample-lib-kotlin-jvm"))
}
