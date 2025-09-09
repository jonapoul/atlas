/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

import org.gradle.declarative.dsl.schema.FqName.Empty.packageName
import org.gradle.testkit.runner.GradleRunner
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Assumptions.assumeFalse
import org.junit.jupiter.api.Assumptions.assumeTrue
import java.io.File

internal fun File.buildRunner(androidHome: File? = null) = GradleRunner
  .create()
  .withPluginClasspath()
  // .withDebug(true)
  .withGradleVersion(System.getProperty("test.version.gradle"))
  .withProjectDir(this)
  .apply {
    if (androidHome != null) {
      withEnvironment(mapOf("ANDROID_HOME" to androidHome.absolutePath))
    }
  }

internal fun File.runTask(task: String, androidHome: File? = null) = buildRunner(androidHome).runTask(task)

internal fun GradleRunner.runTask(task: String) = withArguments(
  task,
  "--configuration-cache",
  // "--info",
  "-Pandroid.useAndroidX=true", // needed for android builds to work, unused otherwise
)

internal fun androidHomeOrSkip(): File {
  val androidHome = System.getProperty("test.androidHome")
  assumeFalse(androidHome.isNullOrBlank())
  val androidHomeFile = File(androidHome)
  assumeTrue(androidHomeFile.exists())
  return androidHomeFile
}

@Language("kotlin")
internal val BASIC_JAVA_BUILD_SCRIPT = """
  plugins {
    id("java")
    id("dev.jonpoulton.modular.leaf")
  }

""".trimIndent()

@Language("kotlin")
internal val BASIC_JVM_BUILD_SCRIPT = """
  plugins {
    kotlin("jvm")
    id("dev.jonpoulton.modular.leaf")
  }
""".trimIndent()

@Language("kotlin")
internal val BASIC_ANDROID_BUILD_SCRIPT = """
  plugins {
    kotlin("android")
    id("com.android.library")
    id("dev.jonpoulton.modular.leaf")
  }

  android {
    namespace = "$packageName"
    compileSdk = 36
  }
""".trimIndent()

@Language("kotlin")
internal const val REPOSITORIES_GRADLE_KTS = """
pluginManagement {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}
"""
