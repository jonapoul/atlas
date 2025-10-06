/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.tasks

import modular.core.InternalModularApi
import modular.core.internal.MODULAR_TASK_GROUP
import modular.core.internal.fileInBuildDirectory
import modular.core.internal.readModuleType
import modular.core.internal.writeModuleTypes
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.ABSOLUTE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

@CacheableTask
abstract class CollateModuleTypes : DefaultTask(), TaskWithOutputFile {
  @get:[PathSensitive(ABSOLUTE) InputFiles] abstract val projectTypeFiles: ConfigurableFileCollection
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    description = "Collates the calculated types of all modules in the project"
  }

  @TaskAction
  fun execute() {
    val outputFile = outputFile.get().asFile
    val typedModules = projectTypeFiles
      .filter { it.exists() }
      .map(::readModuleType)
      .toSortedSet()

    writeModuleTypes(
      modules = typedModules,
      outputFile = outputFile,
    )

    logger.info("CollateModuleTypes: ${typedModules.size} modules")
    typedModules.forEach { typedModule ->
      logger.info("CollateModuleTypes:     typedModule=$typedModule")
    }
  }

  @InternalModularApi
  companion object {
    private const val NAME = "collateModuleTypes"

    @InternalModularApi
    fun get(target: Project): TaskProvider<CollateModuleTypes> =
      target.tasks.named(NAME, CollateModuleTypes::class.java)

    @InternalModularApi
    fun register(target: Project): TaskProvider<CollateModuleTypes> = with(target) {
      val collateTypes = tasks.register(NAME, CollateModuleTypes::class.java) { task ->
        task.outputFile.convention(fileInBuildDirectory("module-types.json"))
      }

      gradle.projectsEvaluated {
        collateTypes.configure { t ->
          val writeTasks = rootProject
            .subprojects
            .toList()
            .mapNotNull(WriteModuleType::get)

          t.dependsOn(writeTasks)

          @Suppress("UnstableApiUsage")
          t.projectTypeFiles.convention(
            writeTasks.map { taskProvider ->
              taskProvider.map { it.outputFile.get() }
            },
          )
        }
      }

      collateTypes
    }
  }
}
