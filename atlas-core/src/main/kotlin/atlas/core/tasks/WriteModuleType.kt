/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.core.tasks

import atlas.core.ModuleType
import atlas.core.ModuleTypeSpec
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.TypedModule
import atlas.core.internal.fileInBuildDirectory
import atlas.core.internal.moduleType
import atlas.core.internal.orderedModuleTypes
import atlas.core.internal.writeModuleType
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
public abstract class WriteModuleType : DefaultTask(), TaskWithOutputFile {
  @get:Input public abstract val projectPath: Property<String>
  @get:[Input Optional] public abstract val moduleType: Property<ModuleType>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = ATLAS_TASK_GROUP
    description = "Caches the module's path and type, for use in other tasks"
  }

  @TaskAction
  public fun execute() {
    val projectPath = projectPath.get()
    val moduleType = moduleType.orNull
    val outputFile = outputFile.get().asFile

    writeModuleType(
      module = TypedModule(projectPath = projectPath, type = moduleType),
      outputFile = outputFile,
    )
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
      extension: AtlasExtensionImpl,
    ): TaskProvider<WriteModuleType> = with(target) {
      val writeModule = tasks.register(NAME, WriteModuleType::class.java) { task ->
        task.projectPath.convention(target.path)
        task.outputFile.convention(fileInBuildDirectory("module-type.json"))
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
        ?: pathMatches.map { path.matches(it.toRegex(regexOptions.orNull.orEmpty())) }.orNull
        ?: hasPluginId.map { pluginManager.hasPlugin(it) }.orNull
        ?: false
    }
  }
}
