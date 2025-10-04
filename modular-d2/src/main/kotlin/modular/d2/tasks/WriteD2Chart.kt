/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.tasks

import modular.core.ModularExtension
import modular.core.Replacement
import modular.core.internal.MODULAR_TASK_GROUP
import modular.core.internal.TypedModules
import modular.core.internal.logIfConfigured
import modular.core.internal.readModuleLinks
import modular.core.tasks.CollateModuleTypes
import modular.core.tasks.ModularGenerationTask
import modular.core.tasks.TaskWithOutputFile
import modular.core.tasks.TaskWithSeparator
import modular.core.tasks.WriteModuleTree
import modular.d2.D2Config
import modular.d2.D2Spec
import modular.d2.internal.D2Writer
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
abstract class WriteD2Chart : WriteD2ChartBase(), ModularGenerationTask {
  override fun getDescription() = "Generates a project dependency graph in d2 format"

  @TaskAction
  override fun execute() {
    super.execute()
    logIfConfigured(outputFile.get().asFile)
  }
}

@DisableCachingByDefault
internal abstract class WriteDummyD2Chart : WriteD2ChartBase() {
  override fun getDescription() = "Generates a dummy d2 chart for comparison against the golden"

  @TaskAction
  override fun execute() = super.execute()
}

@CacheableTask
abstract class WriteD2ChartBase : DefaultTask(), TaskWithSeparator, TaskWithOutputFile {
  // Files
  @get:[PathSensitive(RELATIVE) InputFile] abstract val linksFile: RegularFileProperty
  @get:[PathSensitive(RELATIVE) InputFile] abstract val moduleTypesFile: RegularFileProperty
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  // General
  @get:Input abstract override val separator: Property<String>
  @get:Input abstract val groupModules: Property<Boolean>
  @get:Input abstract val replacements: SetProperty<Replacement>
  @get:Input abstract val thisPath: Property<String>

  // Dotfile config
  @get:Input abstract val config: Property<D2Config>

  init {
    group = MODULAR_TASK_GROUP
    description = "Generates a project dependency graph in d2 format"
  }

  @TaskAction
  open fun execute() {
    val linksFile = linksFile.get().asFile
    val moduleTypesFile = moduleTypesFile.get().asFile
    val separator = separator.get()

    val writer = D2Writer(
      typedModules = TypedModules.read(moduleTypesFile, separator),
      links = readModuleLinks(linksFile, separator),
      replacements = replacements.get(),
      thisPath = thisPath.get(),
      groupModules = groupModules.get(),
      config = config.get(),
    )

    val outputFile = outputFile.get().asFile
    outputFile.writeText(writer())
  }

  internal companion object {
    internal inline fun <reified T : WriteD2ChartBase> register(
      target: Project,
      extension: ModularExtension,
      spec: D2Spec,
      outputFile: File,
    ): TaskProvider<T> = with(target) {
      val collateModuleTypes = CollateModuleTypes.get(rootProject)
      val calculateProjectTree = WriteModuleTree.get(target)

      val qualifier = when (T::class) {
        WriteDummyD2Chart::class -> "Dummy"
        else -> ""
      }
      val name = "write$qualifier${spec.name.capitalized()}Chart"
      val writeChart = tasks.register(name, T::class.java) { task ->
        task.linksFile.convention(calculateProjectTree.map { it.outputFile.get() })
        task.moduleTypesFile.convention(collateModuleTypes.map { it.outputFile.get() })
        task.outputFile.set(outputFile)
        task.thisPath.convention(target.path)
      }

      writeChart.configure { task ->
        task.groupModules.convention(extension.groupModules)
        task.replacements.convention(extension.pathTransforms.replacements)
        task.config.convention(D2Config(spec))
      }

      return writeChart
    }
  }
}
