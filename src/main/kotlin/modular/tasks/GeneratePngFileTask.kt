/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import modular.internal.FILENAME_ROOT
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

@CacheableTask
abstract class GeneratePngFileTask : DefaultTask(), ModularGenerationTask {
  @get:PathSensitive(RELATIVE)
  @get:InputFile
  abstract val dotFile: RegularFileProperty

  @get:OutputFile
  abstract val pngFile: RegularFileProperty

  @get:OutputFile
  abstract val errorFile: RegularFileProperty

  @TaskAction
  fun execute() {
    val dotFile = dotFile.get().asFile.absolutePath
    val pngFile = pngFile.get().asFile
    val errorFile = errorFile.get().asFile

    logger.info("Starting GraphViz for $dotFile")
    val dotProcess = ProcessBuilder("dot", "-Tpng", dotFile)
      .redirectOutput(pngFile)
      .redirectError(errorFile)
      .start()

    val status = dotProcess.waitFor()
    if (status != 0) {
      throw GradleException("GraphViz error code $status: ${errorFile.bufferedReader().readText()}")
    } else {
      logger.lifecycle(pngFile.absolutePath)
      errorFile.delete()
    }
  }

  companion object {
    const val NAME_MODULES: String = "generateModulesPng"
    const val NAME_LEGEND: String = "generateLegendPng"

    fun get(target: Project, name: String): TaskProvider<GeneratePngFileTask> =
      target.tasks.named(name, GeneratePngFileTask::class.java)

    fun registerModules(
      target: Project,
      generateModules: TaskProvider<GenerateModulesDotFileTask>,
    ): TaskProvider<GeneratePngFileTask> = with(target) {
      val legend = get(rootProject, NAME_LEGEND)
      tasks.register(NAME_MODULES, GeneratePngFileTask::class.java) { task ->
        task.group = "reporting"
        task.dotFile.set(generateModules.map { it.outputFile.get() })
        task.pngFile.set(layout.projectDirectory.file("$FILENAME_ROOT.png"))
        task.errorFile.set(layout.projectDirectory.file("$FILENAME_ROOT-error.log"))
        task.dependsOn(legend)
      }
    }

//    fun registerLegend(
//      target: Project,
//      generateLegend: TaskProvider<GenerateLegendDotFileTask>,
//    ): TaskProvider<GeneratePngFileTask> = with(target) {
//      tasks.register(NAME_LEGEND, GeneratePngFileTask::class.java) { task ->
//        task.group = "reporting"
//        task.dotFile.set(generateLegend.map { it.dotFile.get() })
//        task.pngFile.set(layout.projectDirectory.file(GenerateLegendDotFileTask.PNG_PATH))
//        task.errorFile.set(layout.projectDirectory.file("${GenerateLegendDotFileTask.PNG_PATH}.log"))
//      }
//    }
  }
}
