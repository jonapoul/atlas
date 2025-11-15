plugins {
  alias(libs.plugins.agp.lib)
}

android {
  namespace = "dev.jonpoulton.atlas.sample.agp.lib"
  compileSdk = 36
}

dependencies {
  implementation(project(":module-kotlin:jvm"))
  implementation(project(":module-other"))
}
