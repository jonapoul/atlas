/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

import org.gradle.testkit.runner.GradleRunner
import java.io.File

fun File.buildRunner(
  androidHome: File? = null,
  gradleVersion: String = System.getProperty("test.version.gradle"),
): GradleRunner = GradleRunner
  .create()
  .withPluginClasspath()
  // .withDebug(true)
  .withGradleVersion(gradleVersion)
  .withProjectDir(this)
  .apply {
    if (androidHome != null) {
      withEnvironment(mapOf("ANDROID_HOME" to androidHome.absolutePath))
    }
  }

fun File.runTask(
  task: String,
  androidHome: File? = null,
  gradleVersion: String = System.getProperty("test.version.gradle"),
  extras: List<String> = emptyList(),
): GradleRunner = buildRunner(androidHome, gradleVersion).runTask(task, extras)

fun GradleRunner.runTask(
  task: String,
  extras: List<String> = emptyList(),
): GradleRunner = withArguments(
  listOf(
    task,
    "--configuration-cache",
    // "--info",
    // "--stacktrace",
    "-Pandroid.useAndroidX=true", // needed for android builds to work, unused otherwise
  ) + extras,
)
