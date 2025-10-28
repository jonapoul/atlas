import dev.detekt.gradle.Detekt
import dev.detekt.gradle.report.ReportMergeTask

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

val detektReportMergeSarif by tasks.registering(ReportMergeTask::class) {
  output = layout.buildDirectory.file("reports/detekt/merge.sarif.json")
}

tasks.named("check").configure { dependsOn(detektReportMergeSarif) }

allprojects {
  detektReportMergeSarif.configure {
    input.from(tasks.withType<Detekt>().map { it.reports.sarif.outputLocation })
  }
}
