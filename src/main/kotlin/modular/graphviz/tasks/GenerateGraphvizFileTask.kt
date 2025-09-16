/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.tasks

import modular.graphviz.internal.doGraphVizPostProcessing
import modular.graphviz.spec.GraphVizSpec
import modular.internal.ModularExtensionImpl
import modular.internal.Variant
import modular.internal.outputFile
import modular.spec.GeneralFlags
import modular.tasks.MODULAR_TASK_GROUP
import modular.tasks.ModularGenerationTask
import modular.tasks.TaskWithOutputFile
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

@CacheableTask
abstract class GenerateGraphvizFileTask : DefaultTask(), ModularGenerationTask, TaskWithOutputFile {
  @get:[PathSensitive(RELATIVE) InputFile] abstract val dotFile: RegularFileProperty
  @get:Input abstract val outputFormat: Property<String>
  @get:[Input Optional] abstract val pathToDotCommand: Property<String>
  @get:[Input Optional] abstract val engine: Property<String>
  @get:Nested abstract val general: GeneralFlags
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
  }

  // Not using kotlin setter because this pulls a property value
  override fun getDescription() = "Uses GraphViz to convert a dotfile into a ${outputFormat.get()} file"

  @TaskAction
  fun execute() {
    val dotFile = dotFile.get().asFile.absolutePath
    val outputFile = outputFile.get().asFile
    val outputFormat = outputFormat.get()
    val engine = engine.orNull
    val dotCommand = pathToDotCommand.getOrElse("dot")

    val command = buildList {
      add(dotCommand)
      if (engine != null) add("-K$engine")
      add("-T$outputFormat")
      add(dotFile)
    }

    logger.info("Starting GraphViz: $command")
    val dotProcess = ProcessBuilder(command)
      .redirectOutput(outputFile)
      .start()

    val result = dotProcess.waitFor()
    if (result != 0) {
      val fullCommand = command.joinToString(separator = " ")
      val error = dotProcess.errorReader().readText()
      throw GradleException("GraphViz error code $result running '$fullCommand': $error")
    } else {
      logger.lifecycle(outputFile.absolutePath)
    }

    doGraphVizPostProcessing(general, outputFile, outputFormat)
  }

  internal companion object {
    // E.g. format=xdot_json and variant=Legend => "generateLegendXdotJson"
    private fun taskName(variant: Variant, format: String): String {
      val cleanedFormat = format
        .split('-', '_', '.')
        .mapIndexed { i, str -> if (i == 0) str.lowercase() else str.lowercase().replaceFirstChar { it.uppercase() } }
        .joinToString(separator = "")
        .replaceFirstChar { it.uppercase() }
      return "generate" + variant.name + cleanedFormat
    }

    internal fun get(target: Project, name: String): TaskProvider<GenerateGraphvizFileTask> =
      target.tasks.named(name, GenerateGraphvizFileTask::class.java)

    internal fun <T : TaskWithOutputFile> register(
      target: Project,
      extension: ModularExtensionImpl,
      spec: GraphVizSpec,
      variant: Variant,
      dotFileTask: TaskProvider<T>,
    ): List<TaskProvider<GenerateGraphvizFileTask>> = with(target) {
      spec.fileFormats.get().map { format ->
        val outputFile = outputFile(extension.outputs, variant, fileExtension = format)
        val taskName = taskName(variant, format)
        logger.info("Registering $taskName for output format $format")

        tasks.register(taskName, GenerateGraphvizFileTask::class.java) { task ->
          task.dotFile.convention(dotFileTask.map { it.outputFile.get() })
          task.pathToDotCommand.convention(spec.pathToDotCommand)
          task.engine.convention(spec.chart.layoutEngine)
          task.outputFormat.convention(format)
          task.outputFile.convention(outputFile)
          task.general.inject(extension.general)
        }
      }
    }
  }
}
