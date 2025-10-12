plugins {
  id("atlas.convention.kotlin")
  id("atlas.convention.publish")
  alias(libs.plugins.kotlinSerialization)
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(kotlin("stdlib"))
  implementation(libs.kotlinx.serialization)
}
