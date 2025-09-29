/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.tasks

import modular.core.internal.ModularExtensionImpl
import modular.core.internal.TypedModule
import modular.core.internal.fileInBuildDirectory
import modular.core.internal.moduleType
import modular.core.internal.orderedModuleTypes
import modular.core.spec.ModuleType
import modular.core.spec.ModuleTypeSpec
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.UnknownTaskException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

@CacheableTask
abstract class WriteModuleType : DefaultTask(), TaskWithSeparator, TaskWithOutputFile {
  @get:Input abstract val projectPath: Property<String>
  @get:[Input Optional] abstract val moduleType: Property<ModuleType>
  @get:Input abstract override val separator: Property<String>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    description = "Caches the module's path and type, for use in other tasks"
  }

  @TaskAction
  fun execute() {
    val projectPath = projectPath.get()
    val moduleType = moduleType.orNull
    val separator = separator.get()
    val outputFile = outputFile.get().asFile

    val typedModule = TypedModule(projectPath, moduleType)
    outputFile.writeText(typedModule.string(separator))
  }

  internal companion object {
    internal const val NAME = "writeModuleType"

    internal fun get(target: Project): TaskProvider<WriteModuleType>? = try {
      target.tasks.named(NAME, WriteModuleType::class.java)
    } catch (_: UnknownTaskException) {
      null
    }

    internal fun register(
      target: Project,
      extension: ModularExtensionImpl,
    ): TaskProvider<WriteModuleType> = with(target) {
      val writeModule = tasks.register(NAME, WriteModuleType::class.java) { task ->
        task.projectPath.convention(target.path)
        task.outputFile.convention(fileInBuildDirectory("module-type"))
      }

      afterEvaluate {
        val matching = extension
          .orderedModuleTypes()
          .firstOrNull { t -> t.matches(target) }
          ?.let(::moduleType)
        writeModule.configure { t ->
          t.moduleType.convention(matching)
        }
      }

      writeModule
    }

    private fun ModuleTypeSpec.matches(project: Project): Boolean = with(project) {
      pathContains.map { path.contains(it) }.orNull
        ?: pathMatches.map { path.matches(it.toRegex()) }.orNull
        ?: hasPluginId.map { pluginManager.hasPlugin(it) }.orNull
        ?: false
    }
  }
}
