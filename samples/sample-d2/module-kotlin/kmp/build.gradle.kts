plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.agp.lib)
}

kotlin {
  jvm()
  androidTarget()

  sourceSets {
    commonMain.dependencies { api(project(":module-kotlin:jvm")) }
    androidMain.dependencies { api(project(":module-android:lib")) }
    jvmMain.dependencies { implementation(project(":module-java")) }
  }
}

android {
  namespace = "dev.jonpoulton.atlas.sample.kmp"
  compileSdk = 36
}
