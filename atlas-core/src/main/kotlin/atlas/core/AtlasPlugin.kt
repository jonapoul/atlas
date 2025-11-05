package atlas.core

import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.DummyAtlasGenerationTask
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.CheckFileDiff
import atlas.core.tasks.CollateModuleLinks
import atlas.core.tasks.CollateModuleTypes
import atlas.core.tasks.WriteModuleLinks
import atlas.core.tasks.WriteModuleTree
import atlas.core.tasks.WriteModuleType
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import org.gradle.language.base.plugins.LifecycleBasePlugin

/**
 * Base plugin, implemented by the framework modules. This plugin will be applied to the root module by the user, then
 * it will auto-apply itself to all child subprojects internally.
 */
public abstract class AtlasPlugin : Plugin<Project> {
  protected abstract val extension: AtlasExtensionImpl

  protected abstract fun Project.registerRootTasks()
  protected abstract fun Project.registerChildTasks()

  override fun apply(target: Project): Unit = with(target) {
    // This only happens if you have nested modules where the group modules don't have a build file. In that
    // case you don't want the group to be its own node in the chart
    if (!target.buildFile.exists()) return@with

    pluginManager.apply(LifecycleBasePlugin::class.java)

    if (target == rootProject) {
      applyToRoot(target)
    } else {
      applyToChild(target)
    }

    configurePrintFilesToConsole()
    registerAtlasCheckTask()
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

    val atlasGenerate = registerAtlasGenerateTask()
    registerGenerationTaskOnSync(atlasGenerate)

    CollateModuleTypes.get(rootProject).configure { task ->
      task.projectTypeFiles.from(writeType.flatMap { it.outputFile })
    }

    CollateModuleLinks.get(rootProject).configure { task ->
      task.moduleLinkFiles.from(writeLinks.flatMap { it.outputFile })
    }
  }

  private fun Project.warnIfModuleTypesSpecifyNothing() {
    extension.moduleTypes.configureEach { type ->
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

  private fun Project.configureOnDemand() = providers
    .gradleProperty("org.gradle.configureondemand")
    .map { it.toBoolean() }
    .getOrElse(false)

  private fun Project.registerAtlasGenerateTask() = tasks.register("atlasGenerate") { t ->
    t.group = ATLAS_TASK_GROUP
    t.description = "Aggregates all Atlas generation tasks"

    // Always add dependencies first
    t.dependsOn(
      tasks
        .withType(AtlasGenerationTask::class.java)
        .matching { it !is DummyAtlasGenerationTask },
    )

    // Only fail if configureondemand is enabled, this is a subproject, and this specific task was directly called
    // (eg :path:to:atlasGenerate)
    if (configureOnDemand() && project != rootProject) {
      val projectPath = path
      val wasDirectlyInvoked = gradle.startParameter.taskNames.any { it == "$projectPath:atlasGenerate" }
      if (wasDirectlyInvoked) {
        t.doFirst {
          throw GradleException(
            "Warning: atlasGenerate is disabled because org.gradle.configureondemand is enabled. " +
              "With this property set, you can only run atlasGenerate on the root project, not on $projectPath.",
          )
        }
      }
    }
  }

  private fun Project.registerAtlasCheckTask() = tasks.register("atlasCheck") { t ->
    t.group = LifecycleBasePlugin.VERIFICATION_GROUP
    t.description = "Aggregates all Atlas verification tasks"

    // Warn if configureondemand is enabled and this is a subproject
    val projectPath = path
    if (configureOnDemand() && project != rootProject) {
      t.doFirst {
        it.logger.warn(
          "Warning: Nothing was checked because org.gradle.configureondemand is enabled. " +
            "With this property set, you can only run atlasCheck on the root project, not on $projectPath. " +
            "To disable check task registration entirely, set atlas.checkOutputs = false in your build script.",
        )
      }
    } else {
      // Only add dependencies if we're actually going to check
      t.dependsOn(tasks.withType(CheckFileDiff::class.java))
    }
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
