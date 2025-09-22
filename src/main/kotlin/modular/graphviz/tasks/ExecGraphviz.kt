/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.tasks

import modular.core.internal.Variant
import modular.core.tasks.MODULAR_TASK_GROUP
import modular.core.tasks.ModularGenerationTask
import modular.core.tasks.TaskWithOutputFile
import modular.core.tasks.logIfConfigured
import modular.graphviz.internal.GraphvizSpecImpl
import modular.graphviz.internal.doGraphvizPostProcessing
import org.gradle.api.DefaultTask
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
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.process.ExecOperations
import org.gradle.process.ExecSpec
import javax.inject.Inject

@CacheableTask
abstract class ExecGraphviz : DefaultTask(), ModularGenerationTask, TaskWithOutputFile {
  @get:[PathSensitive(RELATIVE) InputFile] abstract val dotFile: RegularFileProperty
  @get:Input abstract val outputFormat: Property<String>
  @get:[Input Optional] abstract val pathToDotCommand: Property<String>
  @get:[Input Optional] abstract val engine: Property<String>
  @get:Input abstract val adjustSvgViewBox: Property<Boolean>
  @get:OutputFile abstract override val outputFile: RegularFileProperty
  @get:Inject abstract val execOperations: ExecOperations

  init {
    group = MODULAR_TASK_GROUP
  }

  // Not using kotlin setter because this pulls a property value
  override fun getDescription() = "Uses Graphviz to convert a dotfile into a ${outputFormat.get()} file"

  @TaskAction
  fun execute() {
    execOperations
      .exec(::configureExec)
      .rethrowFailure()
      .assertNormalExitValue()

    val outputFile = outputFile.get().asFile
    val outputFormat = outputFormat.get()
    logIfConfigured(outputFile)
    doGraphvizPostProcessing(outputFile, outputFormat, adjustSvgViewBox.get())
  }

  private fun configureExec(spec: ExecSpec) {
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

    logger.info("Starting Graphviz: $command")

    spec.commandLine(command)
    spec.standardOutput = outputFile.outputStream()
  }

  internal companion object {
    internal fun get(target: Project, name: String): TaskProvider<ExecGraphviz> =
      target.tasks.named(name, ExecGraphviz::class.java)

    internal fun <T : TaskWithOutputFile> register(
      target: Project,
      spec: GraphvizSpecImpl,
      variant: Variant,
      dotFileTask: TaskProvider<T>,
    ): TaskProvider<ExecGraphviz> = with(target) {
      val format = spec.fileFormat.get()
      val dotFile = dotFileTask.map { it.outputFile.get() }

      val outputFile = dotFile.map { f ->
        val newFile = f.asFile.resolveSibling("${f.asFile.nameWithoutExtension}.$format")
        project.layout.projectDirectory.file(newFile.relativeTo(projectDir).path)
      }

      val name = "exec${spec.name.capitalized()}$variant"
      tasks.register(name, ExecGraphviz::class.java) { task ->
        task.dotFile.convention(dotFile)
        task.pathToDotCommand.convention(spec.pathToDotCommand)
        task.engine.convention(spec.layoutEngine)
        task.outputFormat.convention(format)
        task.outputFile.convention(outputFile)
        task.adjustSvgViewBox.convention(spec.adjustSvgViewBox)
      }
    }
  }
}
