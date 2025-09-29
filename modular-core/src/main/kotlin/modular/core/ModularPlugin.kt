/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core

import modular.core.internal.MODULAR_TASK_GROUP
import modular.core.internal.ModularExtensionImpl
import modular.core.internal.ModularGenerationTask
import modular.core.internal.TaskWithSeparator
import modular.core.internal.orderedModuleTypes
import modular.core.tasks.CollateModuleLinks
import modular.core.tasks.CollateModuleTypes
import modular.core.tasks.WriteModuleLinks
import modular.core.tasks.WriteModuleTree
import modular.core.tasks.WriteModuleType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import kotlin.reflect.KClass

abstract class ModularPlugin<E : ModularExtensionImpl>(private val extensionClass: KClass<E>) : Plugin<Project> {
  @InternalModularApi
  lateinit var extension: E

  protected abstract fun Project.registerRootTasks()
  protected abstract fun Project.registerChildTasks()

  override fun apply(target: Project): Unit = with(target) {
    if (target == rootProject) {
      applyToRoot()
    } else {
      applyToChild()
    }

    configureSeparators()
    configurePrintFilesToConsole()
    val modularGenerate = registerModularGenerateTask()
    registerGenerationTaskOnSync(modularGenerate)
  }

  private fun Project.applyToRoot() {
    require(this == rootProject) {
      error("Modular should only be applied on the root project - you applied it to $path")
    }

    @Suppress("UNCHECKED_CAST")
    extension = extensions.create(
      ModularExtension::class.java,
      ModularExtensionImpl.NAME,
      extensionClass.java,
    ) as E

    subprojects { child ->
      child.pluginManager.apply(this::class.java)
    }

    CollateModuleTypes.register(project)
    CollateModuleLinks.register(project, extension)

    afterEvaluate {
      warnIfModuleTypesSpecifyNothing()
    }
  }

  private fun Project.applyToChild() {
    @Suppress("UNCHECKED_CAST")
    extension = rootProject.extensions.getByType(ModularExtension::class.java) as E

    WriteModuleType.register(project, extension)
    WriteModuleLinks.register(project, extension)
    WriteModuleTree.register(project, extension)
  }

  private fun Project.warnIfModuleTypesSpecifyNothing() {
    extension.orderedModuleTypes().forEach { type ->
      if (!type.pathContains.isPresent && !type.pathMatches.isPresent && !type.hasPluginId.isPresent) {
        logger.warn(
          "Warning: Module type '${type.name}' will be ignored - you need to set one of " +
            "pathContains, pathMatches or hasPluginId.",
        )
      }
    }
  }

  private fun Project.configureSeparators() {
    tasks.withType(TaskWithSeparator::class.java).configureEach { t ->
      t.separator.convention(extension.separator)
    }
  }

  private fun Project.configurePrintFilesToConsole() {
    tasks.withType(ModularGenerationTask::class.java).configureEach { t ->
      t.printFilesToConsole.convention(extension.printFilesToConsole)
    }
  }

  private fun Project.registerModularGenerateTask() = tasks.register("modularGenerate") { t ->
    t.group = MODULAR_TASK_GROUP
    t.description = "Aggregates all Modular generation tasks"
    t.dependsOn(tasks.withType(ModularGenerationTask::class.java))
  }

  private fun Project.registerGenerationTaskOnSync(modularGenerate: TaskProvider<Task>) {
    afterEvaluate {
      val isIntellijSyncing = System.getProperty("idea.sync.active") == "true"
      if (extension.generateOnSync.get() && isIntellijSyncing) {
        tasks.maybeCreate("prepareKotlinIdeaImport").dependsOn(modularGenerate)
      }
    }
  }
}
