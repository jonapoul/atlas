/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test

import assertk.Assert
import assertk.assertions.support.expected
import assertk.fail
import modular.core.internal.diff
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome
import java.io.File
import kotlin.text.removeSuffix

fun Assert<List<BuildTask>>.allSuccessful() = given { tasks ->
  val nonSuccesses = tasks.filter { task -> task.outcome != TaskOutcome.SUCCESS }
  if (nonSuccesses.isEmpty()) return@given
  val successes = tasks.filter { t -> t.outcome == TaskOutcome.SUCCESS }
  fail("Failed allTasksSuccessful: failures=$nonSuccesses, successes=$successes")
}

fun Assert<BuildResult>.taskWasSuccessful(name: String) = taskHadResult(name, expected = TaskOutcome.SUCCESS)

fun Assert<BuildResult>.taskHadResult(name: String, expected: TaskOutcome) =
  given { result -> assertThat(result.task(name)).hadResult(expected) }

fun Assert<BuildTask?>.hadResult(expected: TaskOutcome) = given { result ->
  if (result?.outcome == expected) return@given
  fail("Failed taskHadResult: expected=$expected, actual=${result?.outcome}")
}

fun Assert<File>.contentEquals(expected: String) = given { file ->
  val contents = file.readText().removeSuffix("\n")
  if (contents == expected) return@given
  fail("Failed contentEquals: expected='$expected', actual='$contents'")
}

// Copied from assertk repo but they haven't published in ages
// https://github.com/assertk-org/assertk/blob/main/assertk/src/jvmMain/kotlin/assertk/assertions/file.kt
fun Assert<File>.doesNotExist() = given { actual ->
  if (!actual.exists()) return
  expected("$actual not to exist")
}

fun Assert<String>.equalsDiffed(expected: String) = given { actual ->
  val stripped = actual.removeSuffix("\n")
  if (expected == stripped) return
  expected(
    "Unequal strings between expected{${expected.length}} and actual{${stripped.length}}:\n" + diff(expected, stripped),
  )
}
