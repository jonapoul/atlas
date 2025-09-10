/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import modular.internal.FILENAME_ROOT
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

@CacheableTask
abstract class WriteReadmeTask : DefaultTask() {
  @get:Input abstract val projectPath: Property<String>
  @get:Input abstract val legendPngRelativePath: Property<String>
  @get:OutputFile abstract val readmeFile: RegularFileProperty

  @TaskAction
  fun execute() {
    val legendPngRelativePath = legendPngRelativePath.get()
    val expectedTitle = projectPath.get().removePrefix(prefix = ":")

    val contents = buildString {
      appendLine("# $expectedTitle")
      appendLine("![modules]($FILENAME_ROOT.png)")
      appendLine("![legend]($legendPngRelativePath)")
    }

    readmeFile.asFile.get().writeText(contents)
  }

  companion object {
    fun register(target: Project): TaskProvider<WriteReadmeTask> = with(target) {
//      val modifiedPath = providers
//        .gradleProperty(REMOVE_MODULE_PREFIX)
//        .map { path.removePrefix(it) }

//      val legendPng = rootProject.file(GenerateLegendDotFileTask.PNG_PATH)
//      val projectDir = layout.projectDirectory.asFile
      val legendTask = GenerateLegendDotFileTask.get(rootProject)

      tasks.register("writeReadme", WriteReadmeTask::class.java) { task ->
        task.group = "reporting"
        task.readmeFile.set(file("README.md"))
        task.projectPath.set(path) // TBC: CLEAN PATH
//        task.legendPngRelativePath.set(legendPng.relativeTo(projectDir).toString())
        task.dependsOn(legendTask)
      }
    }
  }
}
