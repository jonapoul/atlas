/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import modular.gradle.ModularExtension
import modular.internal.ModuleLink
import modular.internal.ModuleLinks
import modular.internal.fileInBuildDirectory
import modular.tasks.MODULAR_TASK_GROUP
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

@CacheableTask
abstract class CollateModuleLinksTask : DefaultTask(), TaskWithSeparator, TaskWithOutputFile {
  @get:[PathSensitive(RELATIVE) InputFiles] abstract val moduleLinkFiles: ConfigurableFileCollection
  @get:Input abstract val ignoredModules: SetProperty<Regex>
  @get:Input abstract override val separator: Property<String>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    description = "Collates the links between all modules in the project"
  }

  @TaskAction
  fun execute() {
    val separator = separator.get()
    val outputFile = outputFile.get().asFile

    val links = moduleLinkFiles
      .flatMap { file -> ModuleLinks.read(file, separator) }
      .filterModules()
      .toSet()

    for ((from, to) in links) {
      if (to == from) {
        error("Found a module link from '$from' to itself - this will probably cause weird build issues!")
      }
    }

    ModuleLinks.write(links, outputFile, separator)

    logger.info("CollateModuleLinksTask: ${links.size} links")
    links.forEach { link ->
      logger.info("CollateModuleLinksTask:     from=${link.fromPath}, to=${link.toPath}, config=${link.configuration}")
    }
  }

  private fun List<ModuleLink>.filterModules() = filter { (from, to) ->
    ignoredModules.get().none { pattern ->
      from.matches(pattern) || to.matches(pattern)
    }
  }

  companion object {
    private const val NAME = "collateModuleLinks"

    fun get(target: Project): TaskProvider<CollateModuleLinksTask> =
      target.tasks.named(NAME, CollateModuleLinksTask::class.java)

    fun register(
      target: Project,
      extension: ModularExtension,
    ): TaskProvider<CollateModuleLinksTask> = with(target) {
      val collateLinks = tasks.register(NAME, CollateModuleLinksTask::class.java) { task ->
        task.outputFile.convention(fileInBuildDirectory("module-links"))
        task.ignoredModules.convention(extension.ignoredModules)
      }

      gradle.projectsEvaluated {
        collateLinks.configure { t ->
          val dumpTasks = rootProject
            .subprojects
            .toList()
            .mapNotNull(DumpModuleLinksTask::get)
          t.dependsOn(dumpTasks)

          val linkFiles = dumpTasks.map { provider -> provider.map { it.outputFile.get() } }
          t.moduleLinkFiles.from(linkFiles)
        }
      }

      collateLinks
    }
  }
}
