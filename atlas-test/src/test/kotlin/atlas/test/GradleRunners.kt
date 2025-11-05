package atlas.test

import org.gradle.testkit.runner.GradleRunner
import java.io.File
import kotlin.test.fail

internal fun File.buildRunner(requiresAndroid: Boolean = false): GradleRunner = GradleRunner
  .create()
  .withPluginClasspath()
  .withDebug(false)
  .withGradleVersion(GRADLE_VERSION)
  .withProjectDir(this)
  .apply {
    if (requiresAndroid) {
      if (ANDROID_HOME == null) fail("No ANDROID_HOME supplied!")
      withEnvironment(mapOf("ANDROID_HOME" to ANDROID_HOME.absolutePath))
    }
  }

internal fun File.runTask(
  task: String,
  requiresAndroid: Boolean = false,
  extras: List<String> = emptyList(),
): GradleRunner = buildRunner(requiresAndroid).runTask(task, extras)

internal fun GradleRunner.runTask(
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
