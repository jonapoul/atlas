plugins {
  alias(libs.plugins.agp.lib)
  alias(libs.plugins.kotlinAndroid)
}

android {
  namespace = "dev.jonpoulton.modular.sample.agp.lib"
  compileSdk = 36
}

dependencies {
  implementation(project(":sample-lib-kotlin-jvm"))
}
