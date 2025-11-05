package atlas.core

import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.DummyAtlasGenerationTask
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.CheckFileDiff
import atlas.core.tasks.CollateProjectLinks
import atlas.core.tasks.CollateProjectTypes
import atlas.core.tasks.WriteProjectLinks
import atlas.core.tasks.WriteProjectTree
import atlas.core.tasks.WriteProjectType
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import org.gradle.language.base.plugins.LifecycleBasePlugin

/**
 * Base plugin, implemented by the framework projects. This plugin will be applied to the root project by the user, then
 * it will auto-apply itself to all child subprojects internally.
 */
public abstract class AtlasPlugin : Plugin<Project> {
  protected abstract val extension: AtlasExtensionImpl

  protected abstract fun Project.registerRootTasks()
  protected abstract fun Project.registerChildTasks()

  override fun apply(target: Project): Unit = with(target) {
    // This only happens if you have nested projects where the group projects don't have a build file. In that
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
    CollateProjectTypes.register(project)
    val collateProjectLinks = CollateProjectLinks.register(project, extension)
    registerRootTasks()

    subprojects { child ->
      child.pluginManager.apply(this@AtlasPlugin::class.java)
      child.afterEvaluate {
        child.tasks.withType(WriteProjectTree::class.java).configureEach { t ->
          t.collatedLinks.convention(collateProjectLinks.flatMap { it.outputFile })
        }
      }
    }

    afterEvaluate {
      warnIfProjectTypesSpecifyNothing()
    }
  }

  protected open fun applyToChild(target: Project): Unit = with(target) {
    val writeType = WriteProjectType.register(target, extension)
    val writeLinks = WriteProjectLinks.register(target, extension)
    WriteProjectTree.register(target, extension)
    registerChildTasks()

    val atlasGenerate = registerAtlasGenerateTask()
    registerGenerationTaskOnSync(atlasGenerate)

    CollateProjectTypes.get(rootProject).configure { task ->
      task.projectTypeFiles.from(writeType.flatMap { it.outputFile })
    }

    CollateProjectLinks.get(rootProject).configure { task ->
      task.projectLinkFiles.from(writeLinks.flatMap { it.outputFile })
    }
  }

  private fun Project.warnIfProjectTypesSpecifyNothing() {
    extension.projectTypes.configureEach { type ->
      if (!type.pathContains.isPresent && !type.pathMatches.isPresent && !type.hasPluginId.isPresent) {
        logger.warn(
          "Warning: Project type '${type.name}' will be ignored - you need to set one of " +
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

    // Fail if configureondemand is enabled, this is a subproject, and this specific task was directly called
    // (eg :path:to:atlasGenerate)
    if (configureOnDemand() && project != rootProject) {
      val projectPath = path
      val wasDirectlyInvoked = gradle.startParameter.taskNames.any { it == "$projectPath:atlasGenerate" }
      if (wasDirectlyInvoked) {
        t.doFirst {
          throw GradleException(
            "atlasGenerate is disabled when run on a subproject because org.gradle.configureondemand is enabled. " +
              "With this property set, you can only run atlasGenerate on the root project, not on $projectPath.",
          )
        }
      }
    }
  }

  private fun Project.registerAtlasCheckTask() = tasks.register("atlasCheck") { t ->
    t.group = LifecycleBasePlugin.VERIFICATION_GROUP
    t.description = "Aggregates all Atlas verification tasks"

    // Always add dependencies first
    t.dependsOn(tasks.withType(CheckFileDiff::class.java))

    // Fail if configureondemand is enabled, this is a subproject, and this specific task was directly called
    // (eg :path:to:atlasCheck)
    if (configureOnDemand() && project != rootProject) {
      val projectPath = path
      val wasDirectlyInvoked = gradle.startParameter.taskNames.any { it == "$projectPath:atlasCheck" }
      if (wasDirectlyInvoked) {
        t.doFirst {
          throw GradleException(
            "atlasCheck is disabled when run on a subproject because org.gradle.configureondemand is enabled. " +
              "With this property set, you can only run atlasCheck on the root project, not on $projectPath. " +
              "To disable check task registration entirely, set atlas.checkOutputs = false in your build script.",
          )
        }
      }
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
