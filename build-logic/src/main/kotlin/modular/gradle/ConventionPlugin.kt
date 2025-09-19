package modular.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class ConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    plugins {
      id("modular.convention.kotlin")
      id("java-gradle-plugin")
    }
  }
}
