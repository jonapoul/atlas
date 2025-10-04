plugins {
  id("modular.convention.kotlin")
  id("modular.convention.publish")
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(kotlin("stdlib"))
}
