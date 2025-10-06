/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core

import modular.core.internal.MODULAR_TASK_GROUP
import modular.core.internal.ModularExtensionImpl
import modular.core.internal.orderedModuleTypes
import modular.core.tasks.CollateModuleLinks
import modular.core.tasks.CollateModuleTypes
import modular.core.tasks.ModularGenerationTask
import modular.core.tasks.WriteModuleLinks
import modular.core.tasks.WriteModuleTree
import modular.core.tasks.WriteModuleType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import kotlin.jvm.java

abstract class ModularPlugin<Impl : ModularExtensionImpl> : Plugin<Project> {
  @InternalModularApi
  lateinit var extension: Impl

  protected abstract fun Project.registerRootTasks()
  protected abstract fun Project.registerChildTasks()

  protected abstract fun Project.getExtension(): Impl
  protected abstract fun Project.createExtension(): Impl

  override fun apply(target: Project): Unit = with(target) {
    if (target == rootProject) {
      applyToRoot(target)
    } else {
      applyToChild(target)
    }

    configurePrintFilesToConsole()
    val modularGenerate = registerModularGenerateTask()
    registerGenerationTaskOnSync(modularGenerate)
  }

  protected open fun applyToRoot(target: Project) = with(target) {
    require(target == rootProject) {
      error("Modular should only be applied on the root project - you applied it to $path")
    }

    extension = createExtension()
    CollateModuleTypes.register(project)
    CollateModuleLinks.register(project, extension)
    registerRootTasks()

    subprojects { child ->
      child.pluginManager.apply(this@ModularPlugin::class.java)
    }

    afterEvaluate {
      warnIfModuleTypesSpecifyNothing()
    }
  }

  protected open fun applyToChild(target: Project) = with(target) {
    extension = getExtension()

    WriteModuleType.register(target, extension)
    WriteModuleLinks.register(target, extension)
    WriteModuleTree.register(target, extension)
    registerChildTasks()
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
