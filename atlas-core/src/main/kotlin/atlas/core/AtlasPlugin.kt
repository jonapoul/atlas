/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.core

import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.DummyAtlasGenerationTask
import atlas.core.internal.orderedModuleTypes
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.CheckFileDiff
import atlas.core.tasks.CollateModuleLinks
import atlas.core.tasks.CollateModuleTypes
import atlas.core.tasks.WriteModuleLinks
import atlas.core.tasks.WriteModuleTree
import atlas.core.tasks.WriteModuleType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import org.gradle.language.base.plugins.LifecycleBasePlugin

public abstract class AtlasPlugin : Plugin<Project> {
  protected abstract val extension: AtlasExtensionImpl

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
    val atlasGenerate = registerAtlasGenerateTask()
    registerAtlasCheckTask()
    registerGenerationTaskOnSync(atlasGenerate)
  }

  protected open fun applyToRoot(target: Project): Unit = with(target) {
    CollateModuleTypes.register(project)
    val collateModuleLinks = CollateModuleLinks.register(project, extension)
    registerRootTasks()

    subprojects { child ->
      child.pluginManager.apply(this@AtlasPlugin::class.java)
      child.afterEvaluate {
        child.tasks.withType(WriteModuleTree::class.java).configureEach { t ->
          t.collatedLinks.convention(collateModuleLinks.flatMap { it.outputFile })
        }
      }
    }

    afterEvaluate {
      warnIfModuleTypesSpecifyNothing()
    }
  }

  protected open fun applyToChild(target: Project): Unit = with(target) {
    val writeType = WriteModuleType.register(target, extension)
    val writeLinks = WriteModuleLinks.register(target, extension)
    WriteModuleTree.register(target, extension)
    registerChildTasks()

    CollateModuleTypes.get(rootProject).configure { task ->
      task.projectTypeFiles.from(writeType.flatMap { it.outputFile })
    }

    CollateModuleLinks.get(rootProject).configure { task ->
      task.moduleLinkFiles.from(writeLinks.flatMap { it.outputFile })
    }
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
    tasks.withType(AtlasGenerationTask::class.java).configureEach { t ->
      t.printFilesToConsole.convention(extension.printFilesToConsole)
    }
  }

  private fun Project.registerAtlasGenerateTask() = tasks.register("atlasGenerate") { t ->
    t.group = ATLAS_TASK_GROUP
    t.description = "Aggregates all Atlas generation tasks"
    t.dependsOn(
      tasks
        .withType(AtlasGenerationTask::class.java)
        .matching { it !is DummyAtlasGenerationTask },
    )
  }

  private fun Project.registerAtlasCheckTask() = tasks.register("atlasCheck") { t ->
    t.group = LifecycleBasePlugin.VERIFICATION_GROUP
    t.description = "Aggregates all Atlas verification tasks"
    t.dependsOn(tasks.withType(CheckFileDiff::class.java))
  }

  private fun Project.registerGenerationTaskOnSync(atlasGenerate: TaskProvider<Task>) {
    afterEvaluate {
      val isIntellijSyncing = System.getProperty("idea.sync.active") == "true"
      if (extension.generateOnSync.get() && isIntellijSyncing) {
        tasks.maybeCreate("prepareKotlinIdeaImport").dependsOn(atlasGenerate)
      }
    }
  }
}
