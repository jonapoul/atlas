/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.tasks

import modular.core.InternalModularApi
import modular.core.internal.MODULAR_TASK_GROUP
import modular.core.internal.Variant
import modular.core.internal.logIfConfigured
import modular.core.tasks.ModularGenerationTask
import modular.core.tasks.TaskWithOutputFile
import modular.d2.D2Spec
import modular.d2.FileFormat
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
abstract class ExecD2 : DefaultTask(), ModularGenerationTask, TaskWithOutputFile {
  @get:[PathSensitive(RELATIVE) InputFile] abstract val inputFile: RegularFileProperty
  @get:Input abstract val outputFormat: Property<FileFormat>
  @get:[Input Optional] abstract val pathToD2Command: Property<String>
  @get:OutputFile abstract override val outputFile: RegularFileProperty
  @get:Inject abstract val execOperations: ExecOperations

  init {
    group = MODULAR_TASK_GROUP
  }

  // Not using kotlin setter because this pulls a property value
  override fun getDescription() = "Uses D2 to convert a text diagram into a ${outputFormat.get()} file"

  @TaskAction
  fun execute() {
    execOperations
      .exec(::configureExec)
      .rethrowFailure()
      .assertNormalExitValue()

    logIfConfigured(outputFile.get().asFile)
  }

  private fun configureExec(spec: ExecSpec) {
    val dotFile = inputFile.get().asFile.absolutePath
    val outputFile = outputFile.get().asFile
    val d2Executable = pathToD2Command.getOrElse("d2")

    val command = buildList {
      add(d2Executable)
      add(dotFile)
      add(outputFile)
    }

    logger.info("Starting d2: $command")

    spec.commandLine(command)
    spec.standardOutput = outputFile.outputStream()
  }

  @InternalModularApi
  companion object {
    @InternalModularApi
    fun get(target: Project, name: String): TaskProvider<ExecD2> =
      target.tasks.named(name, ExecD2::class.java)

    @InternalModularApi
    fun <T : TaskWithOutputFile> register(
      target: Project,
      spec: D2Spec,
      variant: Variant,
      dotFileTask: TaskProvider<T>,
    ): TaskProvider<ExecD2> = with(target) {
      val name = "exec${spec.name.capitalized()}$variant"
      val execGraphviz = tasks.register(name, ExecD2::class.java)

      execGraphviz.configure { task ->
        val dotFile = dotFileTask.map { it.outputFile.get() }
        val outputFile = dotFile.map { f ->
          val newFile = f.asFile.resolveSibling("${f.asFile.nameWithoutExtension}.${spec.fileFormat.get()}")
          project.layout.projectDirectory.file(newFile.relativeTo(projectDir).path)
        }

        task.inputFile.convention(dotFile)
        task.pathToD2Command.convention(spec.pathToD2Command)
        task.outputFormat.convention(spec.fileFormat)
        task.outputFile.convention(outputFile)
      }

      return execGraphviz
    }
  }
}
