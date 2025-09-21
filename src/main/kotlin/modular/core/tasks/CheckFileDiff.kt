/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.tasks

import modular.core.internal.Variant
import modular.core.internal.diff
import modular.core.spec.Spec
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.language.base.plugins.LifecycleBasePlugin.VERIFICATION_GROUP

@CacheableTask
abstract class CheckFileDiff : DefaultTask() {
  @get:[PathSensitive(RELATIVE) InputFile] abstract val expectedFile: RegularFileProperty
  @get:[PathSensitive(RELATIVE) InputFile] abstract val actualFile: RegularFileProperty
  @get:Input abstract val taskPath: Property<String>

  init {
    group = VERIFICATION_GROUP
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
    internal fun <T1 : TaskWithOutputFile, T2 : TaskWithOutputFile> register(
      target: Project,
      spec: Spec,
      variant: Variant,
      realTask: TaskProvider<T1>,
      dummyTask: TaskProvider<T2>,
    ): TaskProvider<CheckFileDiff> = with(target) {
      val name = "check${spec.name.capitalized()}$variant"
      tasks.register(name, CheckFileDiff::class.java) { task ->
        task.taskPath.convention(realTask.map { it.path })
        task.actualFile.convention(dummyTask.map { it.outputFile.get() })

        // intentionally doubling the provider here to remove automatic task link between check and write tasks
        task.expectedFile.convention(provider { realTask.map { it.outputFile.get() }.get() })
      }
    }
  }
}
