/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.tasks

import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant
import modular.core.internal.outputFile
import modular.core.tasks.MODULAR_TASK_GROUP
import modular.core.tasks.ModularGenerationTask
import modular.core.tasks.TaskWithOutputFile
import modular.graphviz.internal.GraphVizSpecImpl
import modular.graphviz.internal.doGraphVizPostProcessing
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

@CacheableTask
abstract class RunGraphviz : DefaultTask(), ModularGenerationTask, TaskWithOutputFile {
  @get:[PathSensitive(RELATIVE) InputFile] abstract val dotFile: RegularFileProperty
  @get:Input abstract val outputFormat: Property<String>
  @get:[Input Optional] abstract val pathToDotCommand: Property<String>
  @get:[Input Optional] abstract val engine: Property<String>
  @get:Input abstract val adjustSvgViewBox: Property<Boolean>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    adjustSvgViewBox.convention(false)
  }

  // Not using kotlin setter because this pulls a property value
  override fun getDescription() = "Uses Graphviz to convert a dotfile into a ${outputFormat.get()} file"

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
      logIfConfigured(outputFile)
    }

    doGraphVizPostProcessing(outputFile, outputFormat, adjustSvgViewBox.get())
  }

  internal companion object {
    internal fun get(target: Project, name: String): TaskProvider<RunGraphviz> =
      target.tasks.named(name, RunGraphviz::class.java)

    internal fun <T : TaskWithOutputFile> register(
      target: Project,
      name: String,
      extension: ModularExtensionImpl,
      spec: GraphVizSpecImpl,
      variant: Variant,
      dotFileTask: TaskProvider<T>,
    ): TaskProvider<RunGraphviz> = with(target) {
      val format = spec.fileFormat.get()
      val outputFile = outputFile(extension.outputs, variant, fileExtension = format)

      tasks.register(name, RunGraphviz::class.java) { task ->
        task.dotFile.convention(dotFileTask.map { it.outputFile.get() })
        task.pathToDotCommand.convention(spec.pathToDotCommand)
        task.engine.convention(spec.layoutEngine)
        task.outputFormat.convention(format)
        task.outputFile.convention(outputFile)
        task.adjustSvgViewBox.convention(spec.adjustSvgViewBox)
      }
    }
  }
}
