/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.tasks

import modular.core.internal.ModuleLinks
import modular.core.internal.Replacement
import modular.core.internal.TypedModules
import modular.core.tasks.CollateModuleTypes
import modular.core.tasks.MODULAR_TASK_GROUP
import modular.core.tasks.ModularGenerationTask
import modular.core.tasks.TaskWithOutputFile
import modular.core.tasks.TaskWithSeparator
import modular.core.tasks.WriteModuleTree
import modular.gradle.ModularExtension
import modular.graphviz.internal.DotWriter
import modular.graphviz.spec.DotConfig
import modular.graphviz.spec.GraphVizSpec
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
abstract class WriteGraphvizChart :
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
  @get:Input abstract val config: Property<DotConfig>

  init {
    group = MODULAR_TASK_GROUP
    description = "Generates a project dependency graph in dotfile format"
  }

  @TaskAction
  fun execute() {
    val linksFile = linksFile.get().asFile
    val moduleTypesFile = moduleTypesFile.get().asFile
    val separator = separator.get()

    val writer = DotWriter(
      typedModules = TypedModules.read(moduleTypesFile, separator),
      links = ModuleLinks.read(linksFile, separator),
      replacements = replacements.get(),
      thisPath = thisPath.get(),
      groupModules = groupModules.get(),
      config = config.get(),
    )

    val outputFile = outputFile.get().asFile
    outputFile.writeText(writer())

    logIfConfigured(outputFile)
  }

  internal companion object {
    internal const val TASK_NAME = "writeGraphvizChart"
    internal const val TASK_NAME_FOR_CHECKING = "writeGraphvizChartForChecking"

    internal fun register(
      target: Project,
      name: String,
      extension: ModularExtension,
      spec: GraphVizSpec,
      outputFile: RegularFile,
      printOutput: Boolean,
    ): TaskProvider<WriteGraphvizChart> = with(target) {
      val collateModuleTypes = CollateModuleTypes.get(rootProject)
      val calculateProjectTree = WriteModuleTree.get(target)

      tasks.register(name, WriteGraphvizChart::class.java) { task ->
        task.linksFile.convention(calculateProjectTree.map { it.outputFile.get() })
        task.moduleTypesFile.convention(collateModuleTypes.map { it.outputFile.get() })
        task.outputFile.convention(outputFile)

        task.groupModules.convention(extension.groupModules)
        task.printOutput.convention(printOutput)
        task.replacements.convention(extension.modulePathTransforms.replacements)
        task.thisPath.convention(target.path)

        task.config.convention(provider { DotConfig(spec) })
      }
    }
  }
}
