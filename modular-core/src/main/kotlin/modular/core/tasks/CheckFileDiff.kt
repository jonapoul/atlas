/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("UnstableApiUsage")

package modular.core.tasks

import modular.core.InternalModularApi
import modular.core.ModularExtension
import modular.core.ModularSpec
import modular.core.internal.Variant
import modular.core.internal.diff
import modular.core.internal.problemId
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.problems.Problems
import org.gradle.api.problems.Severity
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.language.base.plugins.LifecycleBasePlugin.CHECK_TASK_NAME
import org.gradle.language.base.plugins.LifecycleBasePlugin.VERIFICATION_GROUP
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject

@CacheableTask
public abstract class CheckFileDiff : DefaultTask() {
  @get:[PathSensitive(RELATIVE) InputFile] public abstract val actualFile: RegularFileProperty
  @get:Input public abstract val expectedDirectory: Property<String>
  @get:Input public abstract val expectedFilename: Property<String>
  @get:Input public abstract val taskPath: Property<String>
  @get:Inject public abstract val problems: Problems

  init {
    group = VERIFICATION_GROUP
    description = "Checks whether two files are equivalent"
  }

  @TaskAction
  public fun execute() {
    val expectedFile = File(expectedDirectory.get(), expectedFilename.get())
    val actualFile = actualFile.get().asFile

    if (!expectedFile.exists()) {
      val e = FileNotFoundException(expectedFile.absolutePath)
      problems.reporter.throwing(e, PROBLEM_DOESNT_EXIST) { spec ->
        with(spec) {
          details("Tried to run comparison on the file at $expectedFile, but it doesn't exist yet.")
          fileLocation(expectedFile.absolutePath)
          solution("Run `gradle ${taskPath.get()}` to generate the file.")
          severity(Severity.ERROR)
          withException(e)
        }
      }
    }

    val expectedContents = expectedFile.readText()
    val actualContents = actualFile.readText()

    if (expectedContents != actualContents) {
      val exception = GradleException("Generated chart file differs from the golden badging file!")
      problems.reporter.throwing(exception, PROBLEM_NEEDS_REGENERATION) { spec ->
        with(spec) {
          details(diff(expectedContents, actualContents))
          fileLocation(expectedFile.absolutePath)
          solution("Run `gradle ${taskPath.get()}` to apply the fixes.")
          severity(Severity.ERROR)
          withException(exception)
        }
      }
    }
  }

  @InternalModularApi
  public companion object {
    private val PROBLEM_DOESNT_EXIST = problemId(
      id = "modular-check-doesnt-exist",
      description = "Expected file doesn't exist",
    )
    private val PROBLEM_NEEDS_REGENERATION = problemId(
      id = "modular-check-regenerate",
      description = "Chart file needs regenerating",
    )

    @InternalModularApi
    public inline fun <reified T1 : TaskWithOutputFile, T2 : TaskWithOutputFile> register(
      target: Project,
      extension: ModularExtension,
      spec: ModularSpec,
      variant: Variant,
      realTask: TaskProvider<T1>,
      dummyTask: TaskProvider<T2>,
    ): TaskProvider<CheckFileDiff> = with(target) {
      val name = "check${spec.name.capitalized()}$variant"
      val checkDiff = tasks.register(name, CheckFileDiff::class.java) { task ->
        task.taskPath.convention(target.path + ":" + realTask.name)
        task.actualFile.convention(dummyTask.map { it.outputFile.get() })
      }

      checkDiff.configure { task ->
        // explicitly splitting like this to force-break the task dependency between write and check
        val expectedFile = realTask
          .map { it.outputFile }
          .get()
          .get()
          .asFile
        task.expectedDirectory.set(expectedFile.parentFile.absolutePath)
        task.expectedFilename.set(expectedFile.name)
      }

      tasks.named(CHECK_TASK_NAME).configure { check ->
        if (extension.checkOutputs.get()) {
          check.dependsOn(checkDiff)
        }
      }

      checkDiff
    }
  }
}
