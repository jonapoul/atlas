plugins {
  alias(libs.plugins.buildConfig) apply false
  alias(libs.plugins.detekt) apply false
  alias(libs.plugins.kotlinJvm) apply false
  alias(libs.plugins.kotlinSerialization) apply false
  alias(libs.plugins.licensee) apply false
  alias(libs.plugins.publish) apply false

  alias(libs.plugins.dokka)
  alias(libs.plugins.publishReport)
}

dokka {
  dokkaPublications.html {
    outputDirectory.set(project.layout.projectDirectory.dir("docs/api"))
    includes.from(project.layout.projectDirectory.file("README.md"))
  }
}

dependencies {
  dokka(project(":atlas-core"))
  dokka(project(":atlas-d2"))
  dokka(project(":atlas-graphviz"))
  dokka(project(":atlas-mermaid"))
}
