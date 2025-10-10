/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.tasks

import modular.core.ModularExtension
import modular.core.Replacement
import modular.core.internal.DummyModularGenerationTask
import modular.core.internal.MODULAR_TASK_GROUP
import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant.Chart
import modular.core.internal.logIfConfigured
import modular.core.internal.modularBuildDirectory
import modular.core.internal.outputFile
import modular.core.internal.qualifier
import modular.core.internal.readModuleLinks
import modular.core.internal.readModuleTypes
import modular.core.tasks.CollateModuleTypes
import modular.core.tasks.ModularGenerationTask
import modular.core.tasks.TaskWithOutputFile
import modular.core.tasks.WriteModuleTree
import modular.graphviz.DotConfig
import modular.graphviz.GraphvizSpec
import modular.graphviz.internal.DotWriter
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
import org.gradle.work.DisableCachingByDefault
import java.io.File

@CacheableTask
abstract class WriteGraphvizChart : DefaultTask(), TaskWithOutputFile, ModularGenerationTask {
  // Files
  @get:[PathSensitive(RELATIVE) InputFile] abstract val linksFile: RegularFileProperty
  @get:[PathSensitive(RELATIVE) InputFile] abstract val moduleTypesFile: RegularFileProperty
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  // General
  @get:Input abstract val groupModules: Property<Boolean>
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

    val writer = DotWriter(
      typedModules = readModuleTypes(moduleTypesFile),
      links = readModuleLinks(linksFile),
      replacements = replacements.get(),
      thisPath = thisPath.get(),
      groupModules = groupModules.get(),
      config = config.get(),
    )

    val outputFile = outputFile.get().asFile
    outputFile.writeText(writer())
    logIfConfigured(outputFile)
  }

  @DisableCachingByDefault
  internal abstract class WriteGraphvizChartDummy : WriteGraphvizChart(), DummyModularGenerationTask

  internal companion object {
    internal fun real(
      target: Project,
      spec: GraphvizSpec,
      extension: ModularExtensionImpl,
    ) = register<WriteGraphvizChart>(
      target = target,
      extension = extension,
      spec = spec,
      outputFile = target.outputFile(Chart, spec.fileExtension.get()),
    )

    internal fun dummy(
      target: Project,
      spec: GraphvizSpec,
      extension: ModularExtensionImpl,
    ) = register<WriteGraphvizChartDummy>(
      target = target,
      extension = extension,
      spec = spec,
      outputFile = target.modularBuildDirectory
        .get()
        .file("chart-temp.dot")
        .asFile,
    )

    private inline fun <reified T : WriteGraphvizChart> register(
      target: Project,
      extension: ModularExtension,
      spec: GraphvizSpec,
      outputFile: File,
    ): TaskProvider<T> = with(target) {
      val collateModuleTypes = CollateModuleTypes.get(rootProject)
      val calculateProjectTree = WriteModuleTree.get(target)
      val name = "write${T::class.qualifier}GraphvizChart"
      val writeChart = tasks.register(name, T::class.java) { task ->
        task.linksFile.convention(calculateProjectTree.map { it.outputFile.get() })
        task.moduleTypesFile.convention(collateModuleTypes.map { it.outputFile.get() })
        task.outputFile.set(outputFile)
        task.thisPath.convention(target.path)
      }

      writeChart.configure { task ->
        task.groupModules.convention(extension.groupModules)
        task.replacements.convention(extension.pathTransforms.replacements)
        task.config.convention(DotConfig(spec))
      }

      return writeChart
    }
  }
}
