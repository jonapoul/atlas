package atlas.d2.tasks

import atlas.core.AtlasExtension
import atlas.core.Replacement
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.DummyAtlasGenerationTask
import atlas.core.internal.logIfConfigured
import atlas.core.internal.qualifier
import atlas.core.internal.readProjectLinks
import atlas.core.internal.readProjectTypes
import atlas.core.tasks.AtlasGenerationTask
import atlas.core.tasks.CollateProjectTypes
import atlas.core.tasks.TaskWithOutputFile
import atlas.core.tasks.WriteProjectTree
import atlas.d2.internal.D2Writer
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
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

@CacheableTask
public abstract class WriteD2Chart : DefaultTask(), TaskWithOutputFile, AtlasGenerationTask {
  // Files
  @get:[PathSensitive(NONE) InputFile] public abstract val linksFile: RegularFileProperty
  @get:[PathSensitive(NONE) InputFile] public abstract val projectTypesFile: RegularFileProperty
  @get:Input public abstract val pathToClassesFile: Property<String>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  // General
  @get:Input public abstract val groupProjects: Property<Boolean>
  @get:Input public abstract val replacements: SetProperty<Replacement>
  @get:Input public abstract val thisPath: Property<String>

  init {
    group = ATLAS_TASK_GROUP
    description = "Generates a project dependency graph in d2 format"
  }

  @TaskAction
  public open fun execute() {
    val linksFile = linksFile.get().asFile
    val projectTypesFile = projectTypesFile.get().asFile
    val outputFile = outputFile.get().asFile
    val pathToClassesFile = pathToClassesFile.get()

    val writer = D2Writer(
      typedProjects = readProjectTypes(projectTypesFile),
      links = readProjectLinks(linksFile),
      replacements = replacements.get(),
      thisPath = thisPath.get(),
      groupProjects = groupProjects.get(),
      pathToClassesFile = pathToClassesFile,
    )

    outputFile.writeText(writer())
    logIfConfigured(outputFile)
  }

  @DisableCachingByDefault
  internal abstract class WriteD2ChartDummy : WriteD2Chart(), DummyAtlasGenerationTask

  internal companion object {
    internal fun real(
      target: Project,
      extension: AtlasExtension,
      outputFile: File,
      pathToClassesFile: Provider<String>,
    ) = register<WriteD2Chart>(target, extension, outputFile, pathToClassesFile)

    internal fun dummy(
      target: Project,
      extension: AtlasExtension,
      outputFile: File,
      pathToClassesFile: Provider<String>,
    ) = register<WriteD2ChartDummy>(target, extension, outputFile, pathToClassesFile)

    private inline fun <reified T : WriteD2Chart> register(
      target: Project,
      extension: AtlasExtension,
      outputFile: File,
      pathToClassesFile: Provider<String>,
    ): TaskProvider<T> = with(target) {
      val collateProjectTypes = CollateProjectTypes.get(rootProject)
      val writeProjectTree = WriteProjectTree.get(target)
      val name = "write${T::class.qualifier}D2Chart"
      val writeChart = tasks.register(name, T::class.java) { task ->
        task.linksFile.convention(writeProjectTree.flatMap { it.outputFile })
        task.projectTypesFile.convention(collateProjectTypes.flatMap { it.outputFile })
        task.outputFile.set(outputFile)
        task.pathToClassesFile.convention(pathToClassesFile)
        task.thisPath.convention(target.path)
      }

      writeChart.configure { task ->
        task.groupProjects.convention(extension.groupProjects)
        task.replacements.convention(extension.pathTransforms.replacements)
      }

      return writeChart
    }
  }
}
