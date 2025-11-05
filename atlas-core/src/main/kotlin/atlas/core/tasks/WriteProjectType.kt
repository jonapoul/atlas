package atlas.core.tasks

import atlas.core.ProjectType
import atlas.core.ProjectTypeSpec
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.TypedProject
import atlas.core.internal.fileInBuildDirectory
import atlas.core.internal.orderedProjectTypes
import atlas.core.internal.projectType
import atlas.core.internal.writeProjectType
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.UnknownTaskException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

/**
 * Dumps the [ProjectTypeSpec] of this project to a file. This will then by aggregated in [CollateProjectTypes].
 */
@CacheableTask
public abstract class WriteProjectType : DefaultTask(), TaskWithOutputFile {
  @get:Input public abstract val projectPath: Property<String>
  @get:[Input Optional] public abstract val projectType: Property<ProjectType>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = ATLAS_TASK_GROUP
    description = "Caches the project's path and type, for use in other tasks"
  }

  @TaskAction
  public fun execute() {
    val projectPath = projectPath.get()
    val projectType = projectType.orNull
    val outputFile = outputFile.get().asFile

    writeProjectType(
      project = TypedProject(projectPath = projectPath, type = projectType),
      outputFile = outputFile,
    )
  }

  internal companion object {
    internal const val NAME = "writeProjectType"

    internal fun get(target: Project): TaskProvider<WriteProjectType>? = try {
      target.tasks.named(NAME, WriteProjectType::class.java)
    } catch (_: UnknownTaskException) {
      null
    }

    internal fun register(
      target: Project,
      extension: AtlasExtensionImpl,
    ): TaskProvider<WriteProjectType> = with(target) {
      val writeProject = tasks.register(NAME, WriteProjectType::class.java) { task ->
        task.projectPath.convention(target.path)
        task.outputFile.convention(fileInBuildDirectory("project-type.json"))
      }

      afterEvaluate {
        val matching = extension
          .orderedProjectTypes()
          .firstOrNull { t -> t.matches(target) }
          ?.let(::projectType)
        writeProject.configure { t ->
          t.projectType.convention(matching)
        }
      }

      writeProject
    }

    private fun ProjectTypeSpec.matches(project: Project): Boolean = with(project) {
      pathContains.map { path.contains(it) }.orNull
        ?: pathMatches.map { path.matches(it.toRegex(regexOptions.orNull.orEmpty())) }.orNull
        ?: hasPluginId.map { pluginManager.hasPlugin(it) }.orNull
        ?: false
    }
  }
}
