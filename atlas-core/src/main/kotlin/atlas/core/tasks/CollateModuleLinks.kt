/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.core.tasks

import atlas.core.AtlasExtension
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.ModuleLink
import atlas.core.internal.fileInBuildDirectory
import atlas.core.internal.readModuleLinks
import atlas.core.internal.writeModuleLinks
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
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
public abstract class CollateModuleLinks : DefaultTask(), TaskWithOutputFile {
  @get:[PathSensitive(RELATIVE) InputFiles] public abstract val moduleLinkFiles: ConfigurableFileCollection
  @get:Input public abstract val ignoredModules: SetProperty<Regex>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = ATLAS_TASK_GROUP
    description = "Collates the links between all modules in the project"
  }

  @TaskAction
  public fun execute() {
    val outputFile = outputFile.get().asFile

    val links = moduleLinkFiles
      .flatMap(::readModuleLinks)
      .filterModules()
      .toSet()

    for ((from, to) in links) {
      if (to == from) {
        error("Found a module link from '$from' to itself - this will probably cause weird build issues!")
      }
    }

    writeModuleLinks(links, outputFile)

    logger.info("CollateModuleLinks: ${links.size} links")
    links.forEach { link ->
      logger.info("CollateModuleLinks:     from=${link.fromPath}, to=${link.toPath}, config=${link.configuration}")
    }
  }

  private fun List<ModuleLink>.filterModules() = filter { (from, to) ->
    ignoredModules.get().none { pattern ->
      from.matches(pattern) || to.matches(pattern)
    }
  }

  internal companion object {
    private const val NAME = "collateModuleLinks"

    internal fun get(target: Project): TaskProvider<CollateModuleLinks> =
      target.tasks.named(NAME, CollateModuleLinks::class.java)

    internal fun register(
      target: Project,
      extension: AtlasExtension,
    ): TaskProvider<CollateModuleLinks> = with(target) {
      val collateLinks = tasks.register(NAME, CollateModuleLinks::class.java) { task ->
        task.outputFile.convention(fileInBuildDirectory("module-links.json"))
        task.ignoredModules.convention(extension.ignoredModules)
      }

      gradle.projectsEvaluated {
        collateLinks.configure { t ->
          val writeTasks = rootProject
            .subprojects
            .toList()
            .mapNotNull(WriteModuleLinks::get)
          t.dependsOn(writeTasks)

          val linkFiles = writeTasks.map { provider -> provider.map { it.outputFile.get() } }
          t.moduleLinkFiles.from(linkFiles)
        }
      }

      collateLinks
    }
  }
}
