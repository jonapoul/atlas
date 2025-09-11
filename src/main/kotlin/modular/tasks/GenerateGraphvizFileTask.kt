/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import modular.gradle.ModularExtension
import modular.gradle.Variant
import modular.internal.MODULAR_TASK_GROUP
import modular.internal.doGraphVizPostProcessing
import modular.internal.outputFile
import modular.spec.DotFileSpec
import modular.spec.ExperimentalFlags
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

@CacheableTask
abstract class GenerateGraphvizFileTask : DefaultTask(), ModularGenerationTask, TaskWithOutputFile {
  @get:[PathSensitive(RELATIVE) InputFile] abstract val dotFile: RegularFileProperty
  @get:Input abstract val outputFormat: Property<String>
  @get:Nested abstract val experimental: ExperimentalFlags
  @get:OutputFile abstract override val outputFile: RegularFileProperty
  @get:OutputFile abstract val errorFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
  }

  // Not using kotlin setter because this pulls a property value
  override fun getDescription() = "Uses GraphViz to convert a dotfile into a ${outputFormat.get()} file"

  @TaskAction
  fun execute() {
    val dotFile = dotFile.get().asFile.absolutePath
    val outputFile = outputFile.get().asFile
    val errorFile = errorFile.get().asFile
    val outputFormat = outputFormat.get()

    val command = listOf("dot", "-T$outputFormat", dotFile)
    logger.info("Starting GraphViz for $dotFile")
    val dotProcess = ProcessBuilder(command)
      .redirectOutput(outputFile)
      .redirectError(errorFile)
      .start()

    val status = dotProcess.waitFor()
    if (status != 0) {
      val fullCommand = command.joinToString(separator = " ")
      val error = errorFile.bufferedReader().readText()
      throw GradleException("GraphViz error code $status running '$fullCommand': $error")
    } else {
      logger.lifecycle(outputFile.absolutePath)
      errorFile.delete()
    }

    doGraphVizPostProcessing(experimental, outputFile, outputFormat)
  }

  companion object {
    // E.g. format=xdot_json and variant=Legend => "generateLegendXdotJson"
    private fun taskName(variant: Variant, format: String): String {
      val cleanedFormat = format
        .split('-', '_', '.')
        .mapIndexed { i, str -> if (i == 0) str.lowercase() else str.lowercase().replaceFirstChar { it.uppercase() } }
        .joinToString(separator = "")
        .replaceFirstChar { it.uppercase() }
      return "generate" + variant.name + cleanedFormat
    }

    fun get(target: Project, name: String): TaskProvider<GenerateGraphvizFileTask> =
      target.tasks.named(name, GenerateGraphvizFileTask::class.java)

    fun <T : TaskWithOutputFile> register(
      target: Project,
      extension: ModularExtension,
      spec: DotFileSpec,
      variant: Variant,
      dotFileTask: TaskProvider<T>,
    ): List<TaskProvider<GenerateGraphvizFileTask>> = with(target) {
      spec.fileFormats.outputFormats.get().map { format ->
        val outputFile = outputFile(extension.outputs, variant, fileExtension = format)
        val errorFile = outputFile(extension.outputs, variant, fileExtension = "$format.log")
        val taskName = taskName(variant, format)
        logger.info("Registering $taskName for output format $format")

        tasks.register(taskName, GenerateGraphvizFileTask::class.java) { task ->
          task.dotFile.set(dotFileTask.map { it.outputFile.get() })
          task.outputFormat.set(format)
          task.outputFile.set(outputFile)
          task.errorFile.set(errorFile)

          with(task.experimental) {
            adjustSvgViewBox.set(extension.experimental.adjustSvgViewBox)
          }
        }
      }
    }
  }
}
