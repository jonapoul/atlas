/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.tasks

import modular.internal.diff
import modular.tasks.MODULAR_TASK_GROUP
import modular.tasks.TaskWithOutputFile
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

@CacheableTask
abstract class CheckDotFileTask : DefaultTask() {
  @get:[PathSensitive(RELATIVE) InputFile] abstract val expectedDotFile: RegularFileProperty
  @get:[PathSensitive(RELATIVE) InputFile] abstract val actualDotFile: RegularFileProperty
  @get:Input abstract val taskPath: Property<String>

  init {
    group = MODULAR_TASK_GROUP
    description = "Checks whether your GraphViz dotfile doesn't need to be regenerated"
  }

  @TaskAction
  fun execute() {
    val expectedDotFile = expectedDotFile.get().asFile
    val actualDotFile = actualDotFile.get().asFile

    val expectedContents = expectedDotFile.readText()
    val actualContents = actualDotFile.readText()

    require(expectedContents == actualContents) {
      buildString {
        appendLine("Dotfile needs updating! Run `gradle ${taskPath.get()}` to regenerate.")
        appendLine("Diff below between $expectedDotFile and $actualDotFile:")
        appendLine()
        appendLine(diff(expectedContents, actualContents))
      }
    }
  }

  internal companion object {
    internal const val NAME_MODULES = "checkModulesDotFile"
    internal const val NAME_LEGEND = "checkLegendDotFile"

    internal fun <T : TaskWithOutputFile> register(
      target: Project,
      name: String,
      generateDotFile: TaskProvider<T>,
      realDotFile: RegularFile,
    ): TaskProvider<CheckDotFileTask> = with(target) {
      tasks.register(name, CheckDotFileTask::class.java) { task ->
        task.taskPath.convention("$path:${GenerateModulesDotFileTask.TASK_NAME}")
        task.expectedDotFile.convention(generateDotFile.map { it.outputFile.get() })
        task.actualDotFile.convention(realDotFile)
      }
    }
  }
}
