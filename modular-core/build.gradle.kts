plugins {
  id("modular.convention.plugin")
}

dependencies {
  compileOnly(gradleApi())
  compileOnly(kotlin("stdlib"))
  testImplementation(project(":modular-test"))
  testPluginClasspath(project(":modular-graphviz"))
  testPluginClasspath(project(":modular-mermaid"))
}
