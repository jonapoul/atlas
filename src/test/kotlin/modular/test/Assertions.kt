/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

import assertk.Assert
import assertk.assertions.support.expected
import assertk.fail
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

internal fun Assert<List<BuildTask>>.allSuccessful() = given { tasks ->
  val nonSuccesses = tasks.filter { task -> task.outcome != TaskOutcome.SUCCESS }
  if (nonSuccesses.isEmpty()) return@given
  fail("Failed allTasksSuccessful: [${nonSuccesses.joinToString()}]")
}

internal fun Assert<BuildResult>.taskWasSuccessful(name: String) = taskHadResult(name, expected = TaskOutcome.SUCCESS)

internal fun Assert<BuildResult>.taskHadResult(name: String, expected: TaskOutcome) =
  given { result -> assertThat(result.task(name)).hadResult(expected) }

internal fun Assert<BuildTask?>.hadResult(expected: TaskOutcome) = given { result ->
  if (result?.outcome == expected) return@given
  fail("Failed taskHadResult: expected=$expected, actual=${result?.outcome}")
}

internal fun Assert<File>.contentEquals(expected: String) = given { file ->
  val contents = file.readText()
  if (contents == expected) return@given
  fail("Failed contentEquals: expected='$expected', actual='$contents'")
}

// Copied from assertk repo but they haven't published in ages
// https://github.com/assertk-org/assertk/blob/main/assertk/src/jvmMain/kotlin/assertk/assertions/file.kt
internal fun Assert<File>.doesNotExist() = given { actual ->
  if (!actual.exists()) return
  expected("not to exist")
}
