/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.tasks

import modular.core.LinkType
import modular.core.internal.MODULAR_TASK_GROUP
import modular.core.internal.ModularExtensionImpl
import modular.core.internal.createModuleLinks
import modular.core.internal.fileInBuildDirectory
import modular.core.internal.orderedLinkTypes
import modular.core.internal.writeModuleLinks
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
abstract class WriteModuleLinks : DefaultTask(), TaskWithOutputFile {
  @get:Input abstract val moduleLinks: MapProperty<String, List<String>>
  @get:Input abstract val linkTypes: SetProperty<LinkType>
  @get:Input abstract val thisPath: Property<String>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    description = "Finds links between this module and its children"
  }

  @TaskAction
  fun execute() {
    val links = writeModuleLinks(
      outputFile = outputFile.get().asFile,
      fromPath = thisPath.get(),
      moduleLinks = moduleLinks.get(),
      linkTypes = linkTypes.get(),
    )

    logger.info("DumpModuleLinks: ${links.size} links")
    links.forEach {
      logger.info("DumpModuleLinks:     link=$it")
    }
  }

  internal companion object {
    private const val NAME = "writeModuleLinks"

    internal fun get(target: Project): TaskProvider<WriteModuleLinks>? = try {
      target.tasks.named(NAME, WriteModuleLinks::class.java)
    } catch (_: UnknownTaskException) {
      null
    }

    internal fun register(
      target: Project,
      extension: ModularExtensionImpl,
    ): TaskProvider<WriteModuleLinks> = with(target) {
      val writeLinks = tasks.register(NAME, WriteModuleLinks::class.java) { task ->
        task.thisPath.convention(target.path)
        task.outputFile.convention(fileInBuildDirectory("module-links.json"))
      }

      writeLinks.configure { task ->
        task.moduleLinks.convention(createModuleLinks(target, extension.ignoredConfigs.get()))
        task.linkTypes.convention(extension.orderedLinkTypes())
      }

      return writeLinks
    }
  }
}
