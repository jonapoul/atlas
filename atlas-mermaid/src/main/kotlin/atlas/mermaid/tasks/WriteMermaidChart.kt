package atlas.mermaid.tasks

import atlas.core.AtlasExtension
import atlas.core.Replacement
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.DummyAtlasGenerationTask
import atlas.core.internal.Variant.Chart
import atlas.core.internal.atlasBuildDirectory
import atlas.core.internal.logIfConfigured
import atlas.core.internal.outputFile
import atlas.core.internal.qualifier
import atlas.core.internal.readProjectLinks
import atlas.core.internal.readProjectTypes
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.CollateProjectTypes
import atlas.core.tasks.TaskWithOutputFile
import atlas.core.tasks.WriteProjectTree
import atlas.mermaid.MermaidConfig
import atlas.mermaid.MermaidSpec
import atlas.mermaid.internal.MermaidWriter
import org.gradle.api.DefaultTask
import org.gradle.api.Project
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
import java.io.File

/**
 * Generates a `.mmd` file containing the Mermaid diagram, which will be then injected into the project's readme.
 */
@CacheableTask
public abstract class WriteMermaidChart : DefaultTask(), AtlasGenerationTask, TaskWithOutputFile {
  // Files
  @get:[PathSensitive(NONE) InputFile] public abstract val linksFile: RegularFileProperty
  @get:[PathSensitive(NONE) InputFile] public abstract val projectTypesFile: RegularFileProperty
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

    val writer = MermaidWriter(
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
    internal fun real(
      target: Project,
      extension: AtlasExtension,
      spec: MermaidSpec,
    ) = register<WriteMermaidChart>(
      target = target,
      extension = extension,
      spec = spec,
      outputFile = target.outputFile(Chart, spec.fileExtension.get()),
    )

    internal fun dummy(
      target: Project,
      extension: AtlasExtension,
      spec: MermaidSpec,
    ) = register<WriteMermaidChartDummy>(
      target = target,
      extension = extension,
      spec = spec,
      outputFile = target.atlasBuildDirectory
        .get()
        .file("chart-temp.mmd")
        .asFile,
    )

    private inline fun <reified T : WriteMermaidChart> register(
      target: Project,
      extension: AtlasExtension,
      spec: MermaidSpec,
      outputFile: File,
    ): TaskProvider<WriteMermaidChart> = with(target) {
      val collateProjectTypes = CollateProjectTypes.get(rootProject)
      val calculateProjectTree = WriteProjectTree.get(target)

      val name = "write${T::class.qualifier}MermaidChart"
      val writeChart = tasks.register(name, WriteMermaidChart::class.java)

      writeChart.configure { task ->
        task.linksFile.convention(calculateProjectTree.map { it.outputFile.get() })
        task.projectTypesFile.convention(collateProjectTypes.map { it.outputFile.get() })
        task.outputFile.set(outputFile)

        task.groupProjects.convention(extension.groupProjects)
        task.replacements.convention(extension.pathTransforms.replacements)
        task.thisPath.convention(target.path)

        task.config.convention(provider { MermaidConfig(extension, spec) })
      }

      return writeChart
    }
  }
}
