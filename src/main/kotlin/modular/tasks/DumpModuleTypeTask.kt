/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import modular.spec.ModuleType
import modular.spec.ModuleTypeModel
import modular.gradle.ModularExtension
import modular.internal.MODULAR_TASK_GROUP
import modular.internal.TypedModule
import modular.internal.fileInReportDirectory
import modular.internal.moduleTypeModel
import modular.internal.orderedTypes
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.UnknownTaskException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register

@CacheableTask
abstract class DumpModuleTypeTask : DefaultTask() {
  @get:Input abstract val projectPath: Property<String>
  @get:[Input Optional] abstract val moduleType: Property<ModuleTypeModel>
  @get:Input abstract val separator: Property<String>
  @get:Input abstract val allModuleTypes: ListProperty<String>
  @get:OutputFile abstract val outputFile: RegularFileProperty

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

    if (moduleType == null) {
      val allModuleTypes = allModuleTypes.get()
      error("No module type matching $projectPath. All types = [${allModuleTypes.joinToString { '"' + it + '"' }}]")
    }

    val typedModule = TypedModule(projectPath, moduleType, separator)
    outputFile.writeText(typedModule.string(separator))
  }

  companion object {
    const val NAME = "dumpModuleType"

    fun get(target: Project): TaskProvider<DumpModuleTypeTask>? = try {
      target.tasks.named<DumpModuleTypeTask>(NAME)
    } catch (_: UnknownTaskException) {
      null
    }

    fun register(
      target: Project,
      extension: ModularExtension,
    ): TaskProvider<DumpModuleTypeTask> = with(target) {
      val task = tasks.register<DumpModuleTypeTask>(NAME) {
        projectPath.set(target.path)
        separator.set(extension.separator)
        outputFile.set(fileInReportDirectory("module-type"))
      }

      afterEvaluate {
        val types = extension.orderedTypes()
        val type = types.firstOrNull { t -> t.matches(target) }
        val matching = type?.let(::moduleTypeModel)
        task.configure { t ->
          t.moduleType.set(matching)
          t.allModuleTypes.set(types.map { it.name })
        }
      }

      task
    }

    private fun ModuleType.matches(project: Project): Boolean = with(project) {
      pathContains.map { path.contains(it) }.orNull
        ?: pathMatches.map { path.matches(it) }.orNull
        ?: hasPluginId.map { pluginManager.hasPlugin(it) }.orNull
        ?: false
    }
  }
}
