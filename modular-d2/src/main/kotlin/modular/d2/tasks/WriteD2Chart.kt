/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.d2.tasks

import modular.core.ModularExtension
import modular.core.Replacement
import modular.core.internal.DummyModularGenerationTask
import modular.core.internal.MODULAR_TASK_GROUP
import modular.core.internal.logIfConfigured
import modular.core.internal.qualifier
import modular.core.internal.readModuleLinks
import modular.core.internal.readModuleTypes
import modular.core.tasks.CollateModuleTypes
import modular.core.tasks.ModularGenerationTask
import modular.core.tasks.TaskWithOutputFile
import modular.core.tasks.WriteModuleTree
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
import org.gradle.work.DisableCachingByDefault
import java.io.File

@CacheableTask
public abstract class WriteD2Chart : DefaultTask(), TaskWithOutputFile, ModularGenerationTask {
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
    group = MODULAR_TASK_GROUP
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
  internal abstract class WriteD2ChartDummy : WriteD2Chart(), DummyModularGenerationTask

  internal companion object {
    internal fun real(
      target: Project,
      extension: ModularExtension,
      outputFile: File,
    ) = register<WriteD2Chart>(target, extension, outputFile)

    internal fun dummy(
      target: Project,
      extension: ModularExtension,
      outputFile: File,
    ) = register<WriteD2ChartDummy>(target, extension, outputFile)

    internal inline fun <reified T : WriteD2Chart> register(
      target: Project,
      extension: ModularExtension,
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
