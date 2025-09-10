/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import modular.internal.MODULAR_TASK_GROUP
import modular.internal.ModuleLinks
import modular.internal.TypedModules
import modular.spec.DotFile
import modular.spec.DotFileChartSpec
import modular.spec.ModuleNameSpec
import modular.spec.RankDir
import modular.spec.Replacement
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
abstract class GenerateModulesDotFileTask : DefaultTask(), TaskWithSeparator {
  // Files
  @get:[PathSensitive(RELATIVE) InputFile] abstract val linksFile: RegularFileProperty
  @get:[PathSensitive(RELATIVE) InputFile] abstract val moduleTypesFile: RegularFileProperty
  @get:OutputFile abstract val outputFile: RegularFileProperty

  // General
  @get:Input abstract val replacements: SetProperty<Replacement>
  @get:Input abstract override val separator: Property<String>
  @get:Input abstract val printOutput: Property<Boolean>

  // Dotfile config
  @get:Input abstract val arrowHead: Property<String>
  @get:Input abstract val arrowTail: Property<String>
  @get:Input abstract val dpi: Property<Int>
  @get:Input abstract val fontSize: Property<Int>
  @get:Input abstract val rankDir: Property<RankDir>
  @get:Input abstract val rankSep: Property<Float>
  @get:Input abstract val showArrows: Property<Boolean>
  @get:Input abstract val thisPath: Property<String>

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
      arrowHead = arrowHead.get(),
      arrowTail = arrowTail.get(),
      dpi = dpi.get(),
      fontSize = fontSize.get(),
      rankDir = rankDir.get(),
      rankSep = rankSep.get(),
      showArrows = showArrows.get(),
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
      moduleNames: ModuleNameSpec,
      spec: DotFileChartSpec,
      dotFile: RegularFile,
      printOutput: Boolean,
    ): TaskProvider<GenerateModulesDotFileTask> = with(target) {
      val collateModuleTypes = CollateModuleTypesTask.get(rootProject)
      val calculateProjectTree = CalculateModuleTreeTask.get(target)

      tasks.register(name, GenerateModulesDotFileTask::class.java) { task ->
        task.linksFile.set(calculateProjectTree.map { it.outputFile.get() })
        task.moduleTypesFile.set(collateModuleTypes.map { it.outputFile.get() })
        task.outputFile.set(dotFile)

        task.replacements.set(moduleNames.replacements)
        task.printOutput.set(printOutput)

        task.arrowHead.set(spec.arrowHead)
        task.arrowTail.set(spec.arrowTail)
        task.dpi.set(spec.dpi)
        task.fontSize.set(spec.fontSize)
        task.rankDir.set(spec.rankDir)
        task.rankSep.set(spec.rankSep)
        task.showArrows.set(spec.showArrows)
        task.thisPath.set(target.path)
      }
    }
  }
}
