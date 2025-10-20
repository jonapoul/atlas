package atlas.test

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.support.expected
import assertk.fail
import atlas.core.internal.diff
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import java.io.File

internal fun Assert<BuildResult>.allTasksSuccessful() = given { result -> assertTasksAllSuccessful(result.tasks) }

internal fun Assert<BuildResult>.noTasksFailed() = given { result ->
  val failures = result.tasks.filter { task -> task.outcome == TaskOutcome.FAILED }
  if (failures.isEmpty()) return
  fail("Failed noTasksFailed: failures=$failures, all=${result.tasks}")
}

internal fun Assert<List<BuildTask>>.allSuccessful() = given(::assertTasksAllSuccessful)

private fun assertTasksAllSuccessful(tasks: List<BuildTask>) {
  val nonSuccesses = tasks.filter { task -> task.outcome != SUCCESS }
  if (nonSuccesses.isEmpty()) return
  val successes = tasks.filter { t -> t.outcome == SUCCESS }
  fail("Failed allTasksSuccessful: failures=$nonSuccesses, successes=$successes")
}

internal fun Assert<BuildResult>.taskWasSuccessful(name: String) = taskHadResult(name, expected = SUCCESS)

internal fun Assert<BuildResult>.taskHadResult(name: String, expected: TaskOutcome) =
  given { result -> assertThat(result.task(name)).hadResult(expected) }

internal fun Assert<BuildTask?>.hadResult(expected: TaskOutcome) = given { result ->
  if (result?.outcome == expected) return@given
  fail("Failed taskHadResult: expected=$expected, actual=${result?.outcome}")
}

internal fun Assert<File>.contentEquals(expected: String) = given { file ->
  val contents = file.readText().removeSuffix("\n")
  if (contents == expected) return@given
  expected(
    "Unequal strings between expected{${expected.length}} and actual{${contents.length}}:\n" + diff(expected, contents),
  )
}

// Copied from assertk repo but they haven't published in ages
// https://github.com/assertk-org/assertk/blob/main/assertk/src/jvmMain/kotlin/assertk/assertions/file.kt
internal fun Assert<File>.doesNotExist() = given { actual ->
  if (!actual.exists()) return
  expected("$actual not to exist")
}

internal fun Assert<String>.equalsDiffed(expected: String) = given { actual ->
  val stripped = actual.removeSuffix("\n")
  if (expected == stripped) return
  expected(
    "Unequal strings between expected{${expected.length}} and actual{${stripped.length}}:\n" + diff(expected, stripped),
  )
}

internal fun <T> Assert<Set<T>>.isEqualToSet(vararg expected: T) = isEqualTo(expected.toSet())
