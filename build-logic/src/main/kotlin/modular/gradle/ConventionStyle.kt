package modular.gradle

import app.cash.licensee.LicenseeExtension
import app.cash.licensee.UnusedAction
import com.diffplug.gradle.spotless.SpotlessExtension
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType

class ConventionStyle : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    detekt()
    licensee()
    spotless()
  }

  private fun Project.detekt() {
    plugins { id("io.gitlab.arturbosch.detekt") }

    extensions.configure<DetektExtension> {
      config.setFrom(file("config/detekt.yml"))
      buildUponDefaultConfig = true
    }

    val detektTasks = tasks.withType<Detekt>()
    val detektCheck by tasks.registering { dependsOn(detektTasks) }

    afterEvaluate {
      val check by tasks
      check.dependsOn(detektCheck)
    }

    detektTasks.configureEach {
      reports.html.required.set(true)
      exclude { it.file.path.contains("generated") }
    }
  }

  private fun Project.licensee() {
    plugins {
      id("org.jetbrains.kotlin.jvm")
      id("app.cash.licensee")
    }

    extensions.configure<LicenseeExtension> {
      unusedAction(UnusedAction.IGNORE)
      listOf("Apache-2.0", "MIT").forEach(::allow)
    }
  }

  private fun Project.spotless() {
    plugins { id("com.diffplug.spotless") }

    extensions.configure<SpotlessExtension> {
      format("misc") {
        target("*.gradle", "*.md", ".gitignore")
        trimTrailingWhitespace()
        leadingTabsToSpaces(2)
        endWithNewline()
      }

      format("licenseKotlin") {
        licenseHeaderFile(rootProject.file("config/spotless.kt"), "(package|@file:)")
        target("src/**/*.kt")
      }
    }
  }
}
