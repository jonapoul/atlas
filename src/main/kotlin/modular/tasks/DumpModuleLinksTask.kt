/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import modular.gradle.ModularExtension
import modular.internal.ModuleLinks
import modular.internal.fileInBuildDirectory
import modular.spec.LinkType
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.UnknownTaskException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

@CacheableTask
abstract class DumpModuleLinksTask : DefaultTask(), TaskWithSeparator, TaskWithOutputFile {
  @get:Input abstract val moduleLinks: MapProperty<String, List<String>>
  @get:Input abstract val linkTypes: SetProperty<LinkType>
  @get:Input abstract val thisPath: Property<String>
  @get:Input abstract override val separator: Property<String>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    description = "Finds links between this module and its children"
  }

  @TaskAction
  fun execute() {
    val links = ModuleLinks.write(
      outputFile = outputFile.get().asFile,
      fromPath = thisPath.get(),
      moduleLinks = moduleLinks.get(),
      linkTypes = linkTypes.get(),
      separator = separator.get(),
    )

    logger.info("DumpModuleLinksTask: ${links.size} links")
    links.forEach { (child, configurations) ->
      logger.info("DumpModuleLinksTask:     child=$child, configurations=[${configurations.joinToString()}]")
    }
  }

  internal companion object {
    private const val NAME = "dumpModuleLinks"

    internal fun get(target: Project): TaskProvider<DumpModuleLinksTask>? = try {
      target.tasks.named(NAME, DumpModuleLinksTask::class.java)
    } catch (_: UnknownTaskException) {
      null
    }

    internal fun register(
      target: Project,
      extension: ModularExtension,
    ): TaskProvider<DumpModuleLinksTask> = with(target) {
      tasks.register(NAME, DumpModuleLinksTask::class.java) { task ->
        task.thisPath.convention(target.path)
        task.moduleLinks.convention(ModuleLinks.of(target, extension.general.ignoredConfigs.get()))
        task.linkTypes.convention(extension.linkTypes.linkTypes)
        task.outputFile.convention(fileInBuildDirectory("module-links"))
      }
    }
  }
}
