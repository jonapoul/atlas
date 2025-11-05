plugins {
  alias(libs.plugins.agp.lib)
}

android {
  namespace = "dev.jonpoulton.atlas.sample.agp.lib"
  compileSdk = 36
}

dependencies {
  implementation(project(":sample-lib-kotlin-jvm"))
}
