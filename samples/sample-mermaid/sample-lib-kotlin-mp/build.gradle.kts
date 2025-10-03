plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.agp.lib)
}

kotlin {
  jvm()
  androidTarget()

  sourceSets {
    commonMain.dependencies { api(project(":sample-lib-kotlin-jvm")) }
    androidMain.dependencies { api(project(":sample-lib-android")) }
    jvmMain.dependencies { implementation(project(":sample-lib-java")) }
  }
}

android {
  namespace = "dev.jonpoulton.modular.sample.kmp"
  compileSdk = 36
}
