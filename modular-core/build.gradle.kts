plugins {
  id("modular.convention.kotlin")
  id("modular.convention.publish")
  alias(libs.plugins.kotlinSerialization)
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(kotlin("stdlib"))
  implementation(libs.kotlinx.serialization)
}
