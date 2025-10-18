/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.mermaid.tasks

import atlas.core.AtlasExtension
import atlas.core.Replacement
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.DummyAtlasGenerationTask
import atlas.core.internal.Variant.Chart
import atlas.core.internal.atlasBuildDirectory
import atlas.core.internal.logIfConfigured
import atlas.core.internal.outputFile
import atlas.core.internal.qualifier
import atlas.core.internal.readModuleLinks
import atlas.core.internal.readModuleTypes
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.CollateModuleTypes
import atlas.core.tasks.TaskWithOutputFile
import atlas.core.tasks.WriteModuleTree
import atlas.mermaid.MermaidConfig
import atlas.mermaid.MermaidSpec
import atlas.mermaid.internal.MermaidWriter
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

/**
 * Generates a `.mmd` file containing the Mermaid diagram, which will be then injected into the module's readme.
 */
@CacheableTask
public abstract class WriteMermaidChart : DefaultTask(), AtlasGenerationTask, TaskWithOutputFile {
  // Files
  @get:[PathSensitive(RELATIVE) InputFile] public abstract val linksFile: RegularFileProperty
  @get:[PathSensitive(RELATIVE) InputFile] public abstract val moduleTypesFile: RegularFileProperty
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  // General
  @get:Input public abstract val groupModules: Property<Boolean>
  @get:Input public abstract val replacements: SetProperty<Replacement>
  @get:Input public abstract val thisPath: Property<String>

  // Mermaid config
  @get:Input public abstract val config: Property<MermaidConfig>

  init {
    group = ATLAS_TASK_GROUP
    description = "Generates a project dependency graph in mermaid format"
  }

  @TaskAction
  public open fun execute() {
    val linksFile = linksFile.get().asFile
    val moduleTypesFile = moduleTypesFile.get().asFile

    val writer = MermaidWriter(
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
  internal abstract class WriteMermaidChartDummy : WriteMermaidChart(), DummyAtlasGenerationTask

  internal companion object {
    internal fun real(
      target: Project,
      extension: AtlasExtension,
      spec: MermaidSpec,
    ) = register<WriteMermaidChart>(
      target = target,
      extension = extension,
      spec = spec,
      outputFile = target.outputFile(Chart, spec.fileExtension.get()),
    )

    internal fun dummy(
      target: Project,
      extension: AtlasExtension,
      spec: MermaidSpec,
    ) = register<WriteMermaidChartDummy>(
      target = target,
      extension = extension,
      spec = spec,
      outputFile = target.atlasBuildDirectory
        .get()
        .file("chart-temp.mmd")
        .asFile,
    )

    private inline fun <reified T : WriteMermaidChart> register(
      target: Project,
      extension: AtlasExtension,
      spec: MermaidSpec,
      outputFile: File,
    ): TaskProvider<WriteMermaidChart> = with(target) {
      val collateModuleTypes = CollateModuleTypes.get(rootProject)
      val calculateProjectTree = WriteModuleTree.get(target)

      val name = "write${T::class.qualifier}MermaidChart"
      val writeChart = tasks.register(name, WriteMermaidChart::class.java)

      writeChart.configure { task ->
        task.linksFile.convention(calculateProjectTree.map { it.outputFile.get() })
        task.moduleTypesFile.convention(collateModuleTypes.map { it.outputFile.get() })
        task.outputFile.set(outputFile)

        task.groupModules.convention(extension.groupModules)
        task.replacements.convention(extension.pathTransforms.replacements)
        task.thisPath.convention(target.path)

        task.config.convention(provider { MermaidConfig(extension, spec) })
      }

      return writeChart
    }
  }
}
