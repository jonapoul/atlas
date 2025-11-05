package atlas.test

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import assertk.assertions.support.expected
import atlas.core.internal.diff
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import java.io.File

internal fun Assert<BuildResult>.allTasksSuccessful(): Assert<List<BuildTask>> =
  prop(BuildResult::getTasks).allSuccessful()

internal fun Assert<BuildResult>.noTasksFailed(): Assert<BuildResult> = transform { result ->
  val failures = result.tasks.filter { task -> task.outcome == TaskOutcome.FAILED }
  if (failures.isEmpty()) {
    result
  } else {
    expected("no tasks to fail: failures=$failures, all=${result.tasks}")
  }
}

internal fun Assert<List<BuildTask>>.allSuccessful(): Assert<List<BuildTask>> = transform { tasks ->
  val nonSuccesses = tasks.filter { task -> task.outcome != SUCCESS }
  if (nonSuccesses.isEmpty()) {
    tasks
  } else {
    val successes = tasks.filter { t -> t.outcome == SUCCESS }
    expected("all tasks to succeed: failures=$nonSuccesses, successes=$successes")
  }
}

internal fun Assert<BuildResult>.taskWasSuccessful(name: String): Assert<BuildResult> =
  taskHadResult(name, expected = SUCCESS)

internal fun Assert<BuildResult>.taskHadResult(
  name: String,
  expected: TaskOutcome,
): Assert<BuildResult> = transform { result ->
  val task = result.task(name)
  if (task?.outcome == expected) {
    result
  } else {
    expected("task result $expected for $name, actual: ${task?.outcome}")
  }
}

internal fun Assert<File>.contentEquals(expected: String): Assert<File> = transform { file ->
  val contents = file.readText().removeSuffix("\n")
  if (contents == expected) {
    file
  } else {
    expected("string with length ${expected.length}, got ${contents.length}:\n" + diff(expected, contents))
  }
}

// Copied from assertk repo but they haven't published in ages
// https://github.com/assertk-org/assertk/blob/main/assertk/src/jvmMain/kotlin/assertk/assertions/file.kt
internal fun Assert<File>.doesNotExist(): Assert<File> = transform { actual ->
  if (!actual.exists()) {
    actual
  } else {
    expected("$actual not to exist")
  }
}

internal fun Assert<String>.equalsDiffed(expected: String): Assert<String> = transform { actual ->
  val stripped = actual.removeSuffix("\n")
  if (expected == stripped) {
    actual
  } else {
    expected("string with length ${expected.length}, got ${stripped.length}:\n" + diff(expected, stripped))
  }
}

internal fun <T> Assert<Set<T>>.isEqualToSet(vararg expected: T) = isEqualTo(expected.toSet())
