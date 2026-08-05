package atlas.core.internal

import atlas.core.internal.AtlasArtifact.Companion.CollatedLinks
import atlas.core.internal.AtlasArtifact.Companion.CollatedTypes
import atlas.core.internal.AtlasArtifact.Companion.ProjectLinks
import atlas.core.internal.AtlasArtifact.Companion.ProjectType as ProjectTypeArtifact
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.CheckFileDiff
import atlas.core.tasks.CollateProjectLinks
import atlas.core.tasks.CollateProjectTypes
import atlas.core.tasks.WriteProjectLinks
import atlas.core.tasks.WriteProjectTree
import atlas.core.tasks.WriteProjectType
import atlas.core.tasks.WriteReadme
import blueprint.core.isIntellijSyncing
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.gradle.language.base.plugins.LifecycleBasePlugin.VERIFICATION_GROUP

internal fun wireProject(target: Project, wiring: AtlasWiring) {
  // Nested projects whose group directories have no build file shouldn't be nodes in the chart
  if (!target.buildFile.exists()) return

  target.pluginManager.apply(LifecycleBasePlugin::class.java)

  val context = AtlasContext(project = target, config = wiring.config, wiring = wiring)
  if (context.isRoot) wireRoot(context) else wireChild(context)

  target.configurePrintFilesToConsole(context)
  target.registerAtlasCheckTask()
}

/** The root collates every subproject's contribution, and draws the shared legends. */
private fun wireRoot(context: AtlasContext) =
  with(context.project) {
    val paths = context.config.subprojectPaths

    val collateTypes =
      CollateProjectTypes.register(
        target = this,
        projectTypeFiles = consumeAtlasArtifact(ProjectTypeArtifact, paths),
      )
    val collateLinks =
      CollateProjectLinks.register(
        target = this,
        config = context.config,
        projectLinkFiles = consumeAtlasArtifact(ProjectLinks, paths),
      )

    publishAtlasArtifact(
      artifact = CollatedTypes,
      file = collateTypes.flatMap { it.outputFile },
      builtBy = collateTypes,
    )
    publishAtlasArtifact(
      artifact = CollatedLinks,
      file = collateLinks.flatMap { it.outputFile },
      builtBy = collateLinks,
    )

    context.config.frameworks.forEach { framework -> framework.tasks.registerRootTasks(context) }
  }

/** Every other project describes itself, then draws its own slice of the graph. */
private fun wireChild(context: AtlasContext) =
  with(context.project) {
    val writeType = WriteProjectType.register(this, context.config)
    val writeLinks = WriteProjectLinks.register(this, context.config)

    publishAtlasArtifact(
      artifact = ProjectTypeArtifact,
      file = writeType.flatMap { it.outputFile },
      builtBy = writeType,
    )
    publishAtlasArtifact(
      artifact = ProjectLinks,
      file = writeLinks.flatMap { it.outputFile },
      builtBy = writeLinks,
    )

    WriteProjectTree.register(
      target = this,
      config = context.config,
      collatedLinks = context.fromRoot(CollatedLinks),
    )

    val atlasGenerate = registerAtlasGenerateTask()
    registerGenerationTaskOnSync(atlasGenerate, context.config)

    val charts =
      context.config.frameworks.map { framework -> framework.tasks.registerChildTasks(context) }
    registerReadmeTask(charts)
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

private fun Project.configurePrintFilesToConsole(context: AtlasContext) {
  tasks.withType(AtlasGenerationTask::class.java).configureEach { task ->
    task.printFilesToConsole.convention(context.config.printFilesToConsole)
  }
}

private fun Project.registerAtlasGenerateTask() =
  tasks.register("atlasGenerate") { task ->
    task.group = ATLAS_TASK_GROUP
    task.description = "Aggregates all Atlas generation tasks"
    task.dependsOn(
      tasks.withType(AtlasGenerationTask::class.java).matching { it !is DummyAtlasGenerationTask }
    )
  }

private fun Project.registerAtlasCheckTask() =
  tasks.register("atlasCheck") { task ->
    task.group = VERIFICATION_GROUP
    task.description = "Aggregates all Atlas verification tasks"
    task.dependsOn(tasks.withType(CheckFileDiff::class.java))
  }

private fun Project.registerGenerationTaskOnSync(
  atlasGenerate: TaskProvider<*>,
  config: AtlasConfig,
) {
  if (!config.generateOnSync) return
  afterEvaluate {
    if (providers.isIntellijSyncing.getOrElse(false)) {
      tasks.maybeCreate("prepareKotlinIdeaImport").dependsOn(atlasGenerate)
    }
  }
}
