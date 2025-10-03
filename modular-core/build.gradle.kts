plugins {
  id("modular.convention.kotlin")
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(kotlin("stdlib"))
}
