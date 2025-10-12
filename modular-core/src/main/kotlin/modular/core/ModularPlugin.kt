/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core

import modular.core.internal.DummyModularGenerationTask
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
import org.gradle.language.base.plugins.LifecycleBasePlugin

public abstract class ModularPlugin : Plugin<Project> {
  protected abstract val extension: ModularExtensionImpl

  protected abstract fun Project.registerRootTasks()
  protected abstract fun Project.registerChildTasks()

  override fun apply(target: Project): Unit = with(target) {
    pluginManager.apply(LifecycleBasePlugin::class.java)

    // This only happens if you have nested modules where the group modules don't have a build file. In that
    // case you don't want the group to be its own node in the chart
    if (!target.buildFile.exists()) return@with

    if (target == rootProject) {
      applyToRoot(target)
    } else {
      applyToChild(target)
    }

    configurePrintFilesToConsole()
    val modularGenerate = registerModularGenerateTask()
    registerGenerationTaskOnSync(modularGenerate)
  }

  protected open fun applyToRoot(target: Project): Unit = with(target) {
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

  protected open fun applyToChild(target: Project): Unit = with(target) {
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
    t.dependsOn(
      tasks
        .withType(ModularGenerationTask::class.java)
        .matching { it !is DummyModularGenerationTask },
    )
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
