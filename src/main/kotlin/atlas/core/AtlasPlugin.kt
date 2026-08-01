package atlas.core

import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.ChartFiles
import atlas.core.internal.DummyAtlasGenerationTask
import atlas.core.internal.LinkTypeSpecImpl
import atlas.core.internal.ProjectTypeSpecImpl
import atlas.core.internal.StyleProperties
import atlas.core.internal.tasks
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.CheckFileDiff
import atlas.core.tasks.CollateProjectLinks
import atlas.core.tasks.CollateProjectTypes
import atlas.core.tasks.WriteProjectLinks
import atlas.core.tasks.WriteProjectTree
import atlas.core.tasks.WriteProjectType
import atlas.core.tasks.WriteReadme
import blueprint.core.boolProperty
import blueprint.core.isIntellijSyncing
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import org.gradle.language.base.plugins.LifecycleBasePlugin

/**
 * Applied to the root project by the user, then auto-applied to all child subprojects internally.
 *
 * Which diagrams get generated is decided by the framework blocks configured in the extension - see
 * [AtlasExtension.d2], [AtlasExtension.graphviz] and [AtlasExtension.mermaid]. Any number of them
 * can be used at once.
 */
public class AtlasPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit =
    with(target) {
      // This only happens if you have nested projects where the group projects don't have a build
      // file. In that case you don't want the group to be its own node in the chart
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

  private fun applyToRoot(target: Project): Unit =
    with(target) {
      val extension =
        extensions.create(
          AtlasExtension::class.java,
          AtlasExtensionImpl.NAME,
          AtlasExtensionImpl::class.java,
        ) as AtlasExtensionImpl

      CollateProjectTypes.register(project)
      val collateProjectLinks = CollateProjectLinks.register(project, extension)

      subprojects { child ->
        child.pluginManager.apply(AtlasPlugin::class.java)
        child.afterEvaluate {
          child.tasks.withType(WriteProjectTree::class.java).configureEach { t ->
            t.collatedLinks.convention(collateProjectLinks.flatMap { it.outputFile })
          }
        }
      }

      // Which frameworks are switched on isn't known until the root build script has been
      // evaluated, so framework tasks are registered here rather than during apply. Root is always
      // evaluated before its children, so subproject tasks still exist by the time anything runs.
      afterEvaluate {
        warnIfProjectTypesSpecifyNothing(extension)
        warnAboutUnusedProperties(extension)
        warnAboutUnsupportedLinkStyles(extension)

        // sorted so that generated output doesn't depend on the order of the config blocks
        val frameworks = extension.frameworks.sorted()
        if (frameworks.isEmpty()) {
          logger.warn(
            "Warning: no Atlas diagram frameworks are configured, so no charts will be generated. " +
              "Add a d2 { }, graphviz { } or mermaid { } block to your atlas { } config."
          )
          return@afterEvaluate
        }

        frameworks.forEach { framework -> framework.tasks.registerRootTasks(project, extension) }

        subprojects { child ->
          if (!child.buildFile.exists()) return@subprojects
          val charts = frameworks.map { framework ->
            framework.tasks.registerChildTasks(child, extension)
          }
          child.registerReadmeTask(charts)
        }
      }
    }

  private fun applyToChild(target: Project): Unit =
    with(target) {
      val extension =
        rootProject.extensions.getByType(AtlasExtension::class.java) as AtlasExtensionImpl

      val writeType = WriteProjectType.register(target, extension)
      val writeLinks = WriteProjectLinks.register(target, extension)
      WriteProjectTree.register(target, extension)

      val atlasGenerate = registerAtlasGenerateTask()
      registerGenerationTaskOnSync(atlasGenerate, extension)

      CollateProjectTypes.get(rootProject).configure { task ->
        task.projectTypeFiles.from(writeType.flatMap { it.outputFile })
      }

      CollateProjectLinks.get(rootProject).configure { task ->
        task.projectLinkFiles.from(writeLinks.flatMap { it.outputFile })
      }
    }

  private fun Project.registerReadmeTask(charts: List<ChartFiles>) {
    val writeReadme = WriteReadme.register(target = this, charts = charts)
    writeReadme.configure { task ->
      charts.forEach { chart ->
        task.dependsOn(chart.chart)
        chart.legend?.let(task::dependsOn)
      }
    }
  }

  private fun Project.warnIfProjectTypesSpecifyNothing(extension: AtlasExtensionImpl) {
    extension.projectTypes.configureEach { type ->
      if (
        !type.pathContains.isPresent && !type.pathMatches.isPresent && !type.hasPluginId.isPresent
      ) {
        logger.warn(
          "Warning: Project type '${type.name}' will be ignored - you need to set one of " +
            "pathContains, pathMatches or hasPluginId."
        )
      }
    }
  }

  /**
   * Every framework's style properties are available on every project and link type, so it's easy
   * to configure one that nothing will read. Point them out rather than silently dropping them.
   */
  private fun Project.warnAboutUnusedProperties(extension: AtlasExtensionImpl) {
    val configured = extension.frameworks

    extension.projectTypes.forEach { type ->
      warnAboutUnusedProperties(
        description = "project type '${type.name}'",
        properties = (type as ProjectTypeSpecImpl).styleProperties,
        configured = configured,
      )
    }

    extension.linkTypes.forEach { type ->
      warnAboutUnusedProperties(
        description = "link type '${type.name}'",
        properties = (type as LinkTypeSpecImpl).styleProperties,
        configured = configured,
      )
    }
  }

  private fun Project.warnAboutUnusedProperties(
    description: String,
    properties: StyleProperties,
    configured: Set<Framework>,
  ) {
    properties.usages
      .filter { usage -> usage.frameworks.none { it in configured } }
      .groupBy({ it.frameworks.sorted() }, { it.name })
      .forEach { (frameworks, names) ->
        val unused = names.distinct()
        val blocks = frameworks.joinToString(separator = " or ") { "${it.string} { }" }
        logger.warn(
          "Warning: $description sets ${unused.joinAnd()}, which only " +
            "${frameworks.map { it.displayName }.joinAnd()} " +
            "${if (frameworks.size == 1) "uses" else "use"}. Configure the $blocks block to use " +
            "${if (unused.size == 1) "it" else "them"}, or remove the config."
        )
      }
  }

  private fun Project.warnAboutUnsupportedLinkStyles(extension: AtlasExtensionImpl) {
    val configured = extension.frameworks
    extension.linkTypes.forEach { type ->
      val style = type.style.orNull ?: return@forEach
      val unsupported = configured.filterNot { it in style.supportedBy }
      if (unsupported.isNotEmpty()) {
        logger.warn(
          "Warning: link type '${type.name}' uses the $style style, which " +
            "${unsupported.map { it.displayName }.joinAnd()} can't draw - " +
            "Atlas will fall back to the closest style it has."
        )
      }
    }
  }

  private fun Project.configurePrintFilesToConsole() {
    val extension = rootProject.extensions.getByType(AtlasExtension::class.java)
    tasks.withType(AtlasGenerationTask::class.java).configureEach { t ->
      t.printFilesToConsole.convention(extension.printFilesToConsole)
    }
  }

  private fun Project.configureOnDemand() =
    providers.boolProperty("org.gradle.configureondemand").getOrElse(false)

  private fun Project.registerAtlasGenerateTask() =
    tasks.register("atlasGenerate") { t ->
      t.group = ATLAS_TASK_GROUP
      t.description = "Aggregates all Atlas generation tasks"

      // Always add dependencies first
      t.dependsOn(
        tasks.withType(AtlasGenerationTask::class.java).matching { it !is DummyAtlasGenerationTask }
      )

      // Fail if configureondemand is enabled, this is a subproject, and this specific task was
      // directly called (eg :path:to:atlasGenerate)
      if (configureOnDemand() && project != rootProject) {
        val projectPath = path
        val wasDirectlyInvoked =
          gradle.startParameter.taskNames.any { it == "$projectPath:atlasGenerate" }
        if (wasDirectlyInvoked) {
          t.doFirst {
            throw GradleException(
              "atlasGenerate is disabled when run on a subproject because org.gradle.configureondemand is enabled. " +
                "With this property set, you can only run atlasGenerate on the root project, not on $projectPath."
            )
          }
        }
      }
    }

  private fun Project.registerAtlasCheckTask() =
    tasks.register("atlasCheck") { t ->
      t.group = LifecycleBasePlugin.VERIFICATION_GROUP
      t.description = "Aggregates all Atlas verification tasks"

      // Always add dependencies first
      t.dependsOn(tasks.withType(CheckFileDiff::class.java))

      // Fail if configureondemand is enabled, this is a subproject, and this specific task was
      // directly called (eg :path:to:atlasCheck)
      if (configureOnDemand() && project != rootProject) {
        val projectPath = path
        val wasDirectlyInvoked =
          gradle.startParameter.taskNames.any { it == "$projectPath:atlasCheck" }
        if (wasDirectlyInvoked) {
          t.doFirst {
            throw GradleException(
              "atlasCheck is disabled when run on a subproject because org.gradle.configureondemand is enabled. " +
                "With this property set, you can only run atlasCheck on the root project, not on $projectPath. " +
                "To disable check task registration entirely, set atlas.checkOutputs = false in your build script."
            )
          }
        }
      }
    }

  private fun Project.registerGenerationTaskOnSync(
    atlasGenerate: TaskProvider<Task>,
    extension: AtlasExtensionImpl,
  ) {
    afterEvaluate {
      val isIntellijSyncing = providers.isIntellijSyncing.getOrElse(false)
      if (extension.generateOnSync.get() && isIntellijSyncing) {
        tasks.maybeCreate("prepareKotlinIdeaImport").dependsOn(atlasGenerate)
      }
    }
  }
}

private fun List<String>.joinAnd(separator: String = "and"): String =
  when (size) {
    0 -> ""
    1 -> single()
    else -> dropLast(1).joinToString(separator = ", ") + " $separator " + last()
  }
