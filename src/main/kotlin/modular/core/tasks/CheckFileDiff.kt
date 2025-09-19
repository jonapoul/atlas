/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.tasks

import modular.core.internal.diff
import modular.graphviz.tasks.WriteGraphvizChart
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
abstract class CheckFileDiff : DefaultTask() {
  @get:[PathSensitive(RELATIVE) InputFile] abstract val expectedFile: RegularFileProperty
  @get:[PathSensitive(RELATIVE) InputFile] abstract val actualFile: RegularFileProperty
  @get:Input abstract val taskPath: Property<String>

  init {
    group = MODULAR_TASK_GROUP
    description = "Checks whether two files are equivalent"
  }

  @TaskAction
  fun execute() {
    val expectedFile = expectedFile.get().asFile
    val actualFile = actualFile.get().asFile

    val expectedContents = expectedFile.readText()
    val actualContents = actualFile.readText()

    require(expectedContents == actualContents) {
      buildString {
        appendLine("File needs updating! Run `gradle ${taskPath.get()}` to regenerate.")
        appendLine("Diff below between $expectedFile and $actualFile:")
        appendLine()
        appendLine(diff(expectedContents, actualContents))
      }
    }
  }

  internal companion object {
    internal fun chartName(flavor: String): String = "check${flavor}Chart"
    internal fun legendName(flavor: String): String = "check${flavor}Legend"

    internal fun <T : TaskWithOutputFile> register(
      target: Project,
      name: String,
      generateTask: TaskProvider<T>,
      realFile: RegularFile,
    ): TaskProvider<CheckFileDiff> = with(target) {
      tasks.register(name, CheckFileDiff::class.java) { task ->
        task.taskPath.convention("$path:${WriteGraphvizChart.TASK_NAME}")
        task.expectedFile.convention(generateTask.map { it.outputFile.get() })
        task.actualFile.convention(realFile)
      }
    }
  }
}
