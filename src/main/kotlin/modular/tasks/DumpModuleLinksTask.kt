/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import modular.gradle.ModularExtension
import modular.internal.MODULAR_TASK_GROUP
import modular.internal.ModuleLinks
import modular.internal.fileInReportDirectory
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.UnknownTaskException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register

@CacheableTask
abstract class DumpModuleLinksTask : DefaultTask() {
  @get:Input abstract val moduleLinks: MapProperty<String, List<String>>
  @get:Input abstract val thisPath: Property<String>
  @get:Input abstract val separator: Property<String>
  @get:OutputFile abstract val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    description = "Finds links between this module and its children."
  }

  @TaskAction
  fun execute() {
    val links = ModuleLinks.write(
      outputFile = outputFile.get().asFile,
      fromPath = thisPath.get(),
      moduleLinks = moduleLinks.get(),
      separator = separator.get(),
    )

    logger.info("DumpModuleLinksTask: ${links.size} links")
    links.forEach { (child, configurations) ->
      logger.info("DumpModuleLinksTask:     child=$child, configurations=[${configurations.joinToString()}]")
    }
  }

  companion object {
    private const val NAME = "dumpModuleLinks"

    fun get(target: Project): TaskProvider<DumpModuleLinksTask>? = try {
      target.tasks.named<DumpModuleLinksTask>(NAME)
    } catch (_: UnknownTaskException) {
      null
    }

    fun register(
      target: Project,
      extension: ModularExtension,
    ): TaskProvider<DumpModuleLinksTask> = with(target) {
      tasks.register<DumpModuleLinksTask>(NAME) {
        thisPath.set(target.path)
        moduleLinks.set(ModuleLinks.of(target, extension.ignoredConfigs.get()))
        outputFile.set(fileInReportDirectory("module-links"))
        separator.set(extension.separator)
      }
    }
  }
}
