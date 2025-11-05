plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.agp.kmp)
}

kotlin {
  jvm()

  android {
    namespace = "dev.jonpoulton.atlas.sample.kmp"
    compileSdk = 36
  }


  sourceSets {
    commonMain.dependencies { api(project(":sample-lib-kotlin-jvm")) }
    androidMain.dependencies { api(project(":sample-lib-android")) }
    jvmMain.dependencies { implementation(project(":sample-lib-java")) }
  }
}
