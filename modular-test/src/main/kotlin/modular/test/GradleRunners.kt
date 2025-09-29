/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

import org.gradle.testkit.runner.GradleRunner
import java.io.File

fun File.buildRunner(androidHome: File? = null): GradleRunner = GradleRunner
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

fun File.runTask(
  task: String,
  androidHome: File? = null,
  extras: List<String> = emptyList(),
): GradleRunner = buildRunner(androidHome).runTask(task, extras)

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
