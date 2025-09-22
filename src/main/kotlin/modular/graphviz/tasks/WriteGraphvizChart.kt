/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.tasks

import modular.core.internal.ModuleLinks
import modular.core.internal.Replacement
import modular.core.internal.TypedModules
import modular.core.internal.Variant
import modular.core.tasks.CollateModuleTypes
import modular.core.tasks.MODULAR_TASK_GROUP
import modular.core.tasks.ModularGenerationTask
import modular.core.tasks.TaskWithOutputFile
import modular.core.tasks.TaskWithSeparator
import modular.core.tasks.WriteModuleTree
import modular.core.tasks.logIfConfigured
import modular.gradle.ModularExtension
import modular.graphviz.internal.DotWriter
import modular.graphviz.spec.DotConfig
import modular.graphviz.spec.GraphvizSpec
import org.gradle.api.DefaultTask
import org.gradle.api.Project
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
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.work.DisableCachingByDefault
import java.io.File

@CacheableTask
abstract class WriteGraphvizChart : WriteGraphvizChartBase(), ModularGenerationTask {
  override fun getDescription() = "Generates a project dependency graph in dotfile format"

  @TaskAction
  override fun execute() {
    super.execute()
    logIfConfigured(outputFile.get().asFile)
  }
}

@DisableCachingByDefault
abstract class WriteDummyGraphvizChart : WriteGraphvizChartBase() {
  override fun getDescription() = "Generates a dummy graphviz chart for comparison against the golden"

  @TaskAction
  override fun execute() = super.execute()
}

@CacheableTask
abstract class WriteGraphvizChartBase : DefaultTask(), TaskWithSeparator, TaskWithOutputFile {
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
  open fun execute() {
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
  }

  internal companion object {
    internal inline fun <reified T : WriteGraphvizChartBase> register(
      target: Project,
      extension: ModularExtension,
      spec: GraphvizSpec,
      variant: Variant,
      outputFile: File,
      printOutput: Boolean,
    ): TaskProvider<T> = with(target) {
      val collateModuleTypes = CollateModuleTypes.get(rootProject)
      val calculateProjectTree = WriteModuleTree.get(target)

      val qualifier = when (T::class) {
        WriteDummyGraphvizChart::class -> "Dummy"
        else -> ""
      }
      val name = "write$qualifier${spec.name.capitalized()}$variant"

      tasks.register(name, T::class.java) { task ->
        task.linksFile.convention(calculateProjectTree.map { it.outputFile.get() })
        task.moduleTypesFile.convention(collateModuleTypes.map { it.outputFile.get() })
        task.outputFile.set(outputFile)

        task.groupModules.convention(extension.groupModules)
        task.printOutput.convention(printOutput)
        task.replacements.convention(extension.modulePathTransforms.replacements)
        task.thisPath.convention(target.path)

        task.config.convention(provider { DotConfig(spec) })
      }
    }
  }
}
