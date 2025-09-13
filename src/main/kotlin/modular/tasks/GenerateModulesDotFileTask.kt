/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import modular.internal.DotFile
import modular.internal.MODULAR_TASK_GROUP
import modular.internal.ModuleLinks
import modular.internal.Replacement
import modular.internal.TypedModules
import modular.spec.DotFileChartSpec
import modular.spec.ModulePathTransformSpec
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
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
  @get:Input abstract val printOutput: Property<Boolean>
  @get:Input abstract val replacements: SetProperty<Replacement>
  @get:Input abstract val thisPath: Property<String>

  // Dotfile config
  @get:[Input Optional] abstract val arrowHead: Property<String>
  @get:[Input Optional] abstract val arrowTail: Property<String>
  @get:[Input Optional] abstract val dir: Property<String>
  @get:[Input Optional] abstract val dpi: Property<Int>
  @get:[Input Optional] abstract val fontSize: Property<Int>
  @get:[Input Optional] abstract val rankDir: Property<String>
  @get:[Input Optional] abstract val rankSep: Property<Float>

  init {
    group = MODULAR_TASK_GROUP
    description = "Generates a project dependency graph dotfile"
  }

  @TaskAction
  fun execute() {
    val linksFile = linksFile.get().asFile
    val moduleTypesFile = moduleTypesFile.get().asFile
    val separator = separator.get()

    val dotFile = DotFile(
      typedModules = TypedModules.read(moduleTypesFile, separator),
      links = ModuleLinks.read(linksFile, separator),
      replacements = replacements.get(),
      thisPath = thisPath.get(),
      arrowHead = arrowHead.orNull,
      arrowTail = arrowTail.orNull,
      dir = dir.orNull,
      dpi = dpi.orNull,
      fontSize = fontSize.orNull,
      rankDir = rankDir.orNull,
      rankSep = rankSep.orNull,
    )

    val outputFile = outputFile.get().asFile
    outputFile.writeText(dotFile())

    if (printOutput.get()) {
      logger.lifecycle(outputFile.absolutePath)
    }
  }

  companion object {
    const val TASK_NAME: String = "generateModulesDotFile"

    fun register(
      target: Project,
      name: String,
      modulePathTransforms: ModulePathTransformSpec,
      spec: DotFileChartSpec,
      outputFile: RegularFile,
      printOutput: Boolean,
    ): TaskProvider<GenerateModulesDotFileTask> = with(target) {
      val collateModuleTypes = CollateModuleTypesTask.get(rootProject)
      val calculateProjectTree = CalculateModuleTreeTask.get(target)

      tasks.register(name, GenerateModulesDotFileTask::class.java) { task ->
        task.linksFile.convention(calculateProjectTree.map { it.outputFile.get() })
        task.moduleTypesFile.convention(collateModuleTypes.map { it.outputFile.get() })
        task.outputFile.convention(outputFile)

        task.replacements.convention(modulePathTransforms.get())
        task.printOutput.convention(printOutput)

        task.arrowHead.convention(spec.arrowHead)
        task.arrowTail.convention(spec.arrowTail)
        task.dpi.convention(spec.dpi)
        task.fontSize.convention(spec.fontSize)
        task.rankDir.convention(spec.rankDir)
        task.rankSep.convention(spec.rankSep)
        task.dir.convention(spec.dir)
        task.thisPath.convention(target.path)
      }
    }
  }
}
