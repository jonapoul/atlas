/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.tasks

import modular.gradle.ModularExtension
import modular.graphviz.internal.DotFileWriter
import modular.graphviz.spec.DotFileConfig
import modular.graphviz.spec.GraphVizSpec
import modular.internal.ModuleLinks
import modular.internal.Replacement
import modular.internal.TypedModules
import modular.tasks.CalculateModuleTreeTask
import modular.tasks.CollateModuleTypesTask
import modular.tasks.MODULAR_TASK_GROUP
import modular.tasks.ModularGenerationTask
import modular.tasks.TaskWithOutputFile
import modular.tasks.TaskWithSeparator
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

@CacheableTask
abstract class GenerateModulesDotFileTask :
  DefaultTask(),
  TaskWithSeparator,
  ModularGenerationTask,
  TaskWithOutputFile {
  // Files
  @get:[PathSensitive(RELATIVE) InputFile] abstract val linksFile: RegularFileProperty
  @get:[PathSensitive(RELATIVE) InputFile] abstract val moduleTypesFile: RegularFileProperty
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  // General
  @get:Input abstract override val separator: Property<String>
  @get:Input abstract val groupModules: Property<Boolean>
  @get:Input abstract val printOutput: Property<Boolean>
  @get:Input abstract val replacements: SetProperty<Replacement>
  @get:Input abstract val thisPath: Property<String>

  // Dotfile config
  @get:Input abstract val config: Property<DotFileConfig>

  init {
    group = MODULAR_TASK_GROUP
    description = "Generates a project dependency graph dotfile"
  }

  @TaskAction
  fun execute() {
    val linksFile = linksFile.get().asFile
    val moduleTypesFile = moduleTypesFile.get().asFile
    val separator = separator.get()

    val writer = DotFileWriter(
      typedModules = TypedModules.read(moduleTypesFile, separator),
      links = ModuleLinks.read(linksFile, separator),
      replacements = replacements.get(),
      thisPath = thisPath.get(),
      groupModules = groupModules.get(),
      config = config.get(),
    )

    val outputFile = outputFile.get().asFile
    outputFile.writeText(writer())

    if (printOutput.get()) {
      logger.lifecycle(outputFile.absolutePath)
    }
  }

  companion object {
    internal const val TASK_NAME = "generateModulesDotFile"
    internal const val TASK_NAME_FOR_CHECKING = "generateModulesDotFileForChecking"

    fun register(
      target: Project,
      name: String,
      extension: ModularExtension,
      spec: GraphVizSpec,
      outputFile: RegularFile,
      printOutput: Boolean,
    ): TaskProvider<GenerateModulesDotFileTask> = with(target) {
      val collateModuleTypes = CollateModuleTypesTask.get(rootProject)
      val calculateProjectTree = CalculateModuleTreeTask.get(target)

      tasks.register(name, GenerateModulesDotFileTask::class.java) { task ->
        task.linksFile.convention(calculateProjectTree.map { it.outputFile.get() })
        task.moduleTypesFile.convention(collateModuleTypes.map { it.outputFile.get() })
        task.outputFile.convention(outputFile)

        task.groupModules.convention(extension.general.groupModules)
        task.printOutput.convention(printOutput)
        task.replacements.convention(extension.modulePathTransforms.replacements)
        task.thisPath.convention(target.path)

        task.config.convention(provider { DotFileConfig(spec.chart) })
      }
    }
  }
}
