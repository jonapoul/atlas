package atlas.mermaid.tasks

import atlas.core.Framework.Mermaid
import atlas.core.Replacement
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.AtlasArtifact
import atlas.core.internal.AtlasContext
import atlas.core.internal.DummyAtlasGenerationTask
import atlas.core.internal.Variant.Chart
import atlas.core.internal.atlasBuildDirectory
import atlas.core.internal.logIfConfigured
import atlas.core.internal.outputFile
import atlas.core.internal.qualifier
import atlas.core.internal.readProjectLinks
import atlas.core.internal.readProjectTypes
import atlas.core.internal.singleFile
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.TaskWithOutputFile
import atlas.core.tasks.WriteProjectTree
import atlas.mermaid.MermaidConfig
import atlas.mermaid.MermaidSpec
import atlas.mermaid.internal.MermaidWriter
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.NONE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.work.DisableCachingByDefault

/**
 * Generates a `.mmd` file containing the Mermaid diagram, which will be then injected into the
 * project's readme.
 */
@CacheableTask
public abstract class WriteMermaidChart : DefaultTask(), AtlasGenerationTask, TaskWithOutputFile {
  // Files
  @get:[PathSensitive(NONE) InputFile]
  public abstract val linksFile: RegularFileProperty
  @get:[PathSensitive(NONE) InputFile]
  public abstract val projectTypesFile: RegularFileProperty
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  // General
  @get:Input public abstract val groupProjects: Property<Boolean>
  @get:Input public abstract val replacements: SetProperty<Replacement>
  @get:Input public abstract val thisPath: Property<String>

  // Mermaid config
  @get:Input public abstract val config: Property<MermaidConfig>

  init {
    group = ATLAS_TASK_GROUP
    description = "Generates a project dependency graph in mermaid format"
  }

  @TaskAction
  public open fun execute() {
    val linksFile = linksFile.get().asFile
    val projectTypesFile = projectTypesFile.get().asFile

    val writer =
      MermaidWriter(
        typedProjects = readProjectTypes(projectTypesFile),
        links = readProjectLinks(linksFile),
        replacements = replacements.get(),
        thisPath = thisPath.get(),
        groupProjects = groupProjects.get(),
        config = config.get(),
      )

    val outputFile = outputFile.get().asFile
    outputFile.writeText(writer())
    logIfConfigured(outputFile)
  }

  @DisableCachingByDefault
  internal abstract class WriteMermaidChartDummy : WriteMermaidChart(), DummyAtlasGenerationTask

  internal companion object {
    internal fun real(context: AtlasContext, spec: MermaidSpec) =
      register<WriteMermaidChart>(
        context = context,
        spec = spec,
        outputFile =
          context.project.outputFile(
            config = context.config,
            framework = Mermaid,
            variant = Chart,
            fileExtension = spec.fileExtension.get(),
          ),
      )

    internal fun dummy(context: AtlasContext, spec: MermaidSpec) =
      register<WriteMermaidChartDummy>(
        context = context,
        spec = spec,
        outputFile = context.project.atlasBuildDirectory.get().file("chart-temp.mmd").asFile,
      )

    private inline fun <reified T : WriteMermaidChart> register(
      context: AtlasContext,
      spec: MermaidSpec,
      outputFile: File,
    ): TaskProvider<WriteMermaidChart> =
      with(context.project) {
        val collatedTypes = context.fromRoot(AtlasArtifact.CollatedTypes)
        val calculateProjectTree = WriteProjectTree.get(this)

        val name = "write${T::class.qualifier}MermaidChart"
        val writeChart = tasks.register(name, WriteMermaidChart::class.java)

        writeChart.configure { task ->
          task.linksFile.convention(calculateProjectTree.flatMap { it.outputFile })
          task.projectTypesFile.fileProvider(collatedTypes.singleFile(AtlasArtifact.CollatedTypes))
          task.dependsOn(collatedTypes)
          task.outputFile.set(outputFile)

          task.groupProjects.convention(context.config.groupProjects)
          task.replacements.convention(context.config.replacements)
          task.thisPath.convention(path)

          task.config.convention(provider { MermaidConfig(context.config, spec) })
        }

        return writeChart
      }
  }
}
