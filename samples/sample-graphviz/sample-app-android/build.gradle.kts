plugins {
  alias(libs.plugins.agp.app)
  alias(libs.plugins.kotlinAndroid)
}

android {
  namespace = "dev.jonpoulton.atlas.sample.agp.app"
  compileSdk = 36
}

dependencies {
  implementation(project(":sample-lib-android"))
  implementation(project(":sample-lib-kotlin-mp"))
}
