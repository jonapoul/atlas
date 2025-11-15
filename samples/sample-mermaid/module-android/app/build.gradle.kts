plugins {
  alias(libs.plugins.agp.app)
}

android {
  namespace = "dev.jonpoulton.atlas.sample.agp.app"
  compileSdk = 36
}

dependencies {
  implementation(project(":module-android:lib"))
  implementation(project(":module-kotlin:kmp"))
}
