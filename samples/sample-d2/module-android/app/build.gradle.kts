plugins {
  alias(libs.plugins.agp.app)
  alias(libs.plugins.kotlinAndroid)
}

android {
  namespace = "dev.jonpoulton.modular.sample.agp.app"
  compileSdk = 36
}

dependencies {
  implementation(project(":module-android:lib"))
  implementation(project(":module-kotlin:kmp"))
}
