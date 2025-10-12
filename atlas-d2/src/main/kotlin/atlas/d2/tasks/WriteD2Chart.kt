/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.d2.tasks

import atlas.core.AtlasExtension
import atlas.core.Replacement
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.DummyAtlasGenerationTask
import atlas.core.internal.logIfConfigured
import atlas.core.internal.qualifier
import atlas.core.internal.readModuleLinks
import atlas.core.internal.readModuleTypes
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.CollateModuleTypes
import atlas.core.tasks.TaskWithOutputFile
import atlas.core.tasks.WriteModuleTree
import atlas.d2.internal.D2Writer
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
public abstract class WriteD2Chart : DefaultTask(), TaskWithOutputFile, AtlasGenerationTask {
  // Files
  @get:[PathSensitive(RELATIVE) InputFile] public abstract val globalFile: RegularFileProperty
  @get:[PathSensitive(RELATIVE) InputFile] public abstract val linksFile: RegularFileProperty
  @get:[PathSensitive(RELATIVE) InputFile] public abstract val moduleTypesFile: RegularFileProperty
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  // General
  @get:Input public abstract val groupModules: Property<Boolean>
  @get:Input public abstract val replacements: SetProperty<Replacement>
  @get:Input public abstract val thisPath: Property<String>

  init {
    group = ATLAS_TASK_GROUP
    description = "Generates a project dependency graph in d2 format"
  }

  @TaskAction
  public open fun execute() {
    val globalFile = globalFile.get().asFile
    val linksFile = linksFile.get().asFile
    val moduleTypesFile = moduleTypesFile.get().asFile
    val outputFile = outputFile.get().asFile

    val writer = D2Writer(
      typedModules = readModuleTypes(moduleTypesFile),
      links = readModuleLinks(linksFile),
      replacements = replacements.get(),
      thisPath = thisPath.get(),
      groupModules = groupModules.get(),
      globalRelativePath = globalFile.relativeTo(outputFile.parentFile).path,
    )

    outputFile.writeText(writer())
    logIfConfigured(outputFile)
  }

  @DisableCachingByDefault
  internal abstract class WriteD2ChartDummy : WriteD2Chart(), DummyAtlasGenerationTask

  internal companion object {
    internal fun real(
      target: Project,
      extension: AtlasExtension,
      outputFile: File,
    ) = register<WriteD2Chart>(target, extension, outputFile)

    internal fun dummy(
      target: Project,
      extension: AtlasExtension,
      outputFile: File,
    ) = register<WriteD2ChartDummy>(target, extension, outputFile)

    internal inline fun <reified T : WriteD2Chart> register(
      target: Project,
      extension: AtlasExtension,
      outputFile: File,
    ): TaskProvider<T> = with(target) {
      val collateModuleTypes = CollateModuleTypes.get(rootProject)
      val calculateProjectTree = WriteModuleTree.get(target)
      val writeD2Classes = WriteD2Classes.get(rootProject)
      val name = "write${T::class.qualifier}D2Chart"
      val writeChart = tasks.register(name, T::class.java) { task ->
        task.globalFile.convention(writeD2Classes.map { it.outputFile.get() })
        task.linksFile.convention(calculateProjectTree.map { it.outputFile.get() })
        task.moduleTypesFile.convention(collateModuleTypes.map { it.outputFile.get() })
        task.outputFile.set(outputFile)
        task.thisPath.convention(target.path)
      }

      writeChart.configure { task ->
        task.groupModules.convention(extension.groupModules)
        task.replacements.convention(extension.pathTransforms.replacements)
      }

      return writeChart
    }
  }
}
