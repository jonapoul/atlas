plugins {
  id("modular.convention.kotlin")
  id("modular.convention.publish")
  id("modular.convention.test")
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(kotlin("stdlib"))
  testImplementation(project(":modular-test"))
}
