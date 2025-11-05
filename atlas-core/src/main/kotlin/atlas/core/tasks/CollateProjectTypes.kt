package atlas.core.tasks

import atlas.core.InternalAtlasApi
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.fileInBuildDirectory
import atlas.core.internal.readProjectType
import atlas.core.internal.writeProjectTypes
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.NONE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

/**
 * Registered on the root project to aggregate the results of [WriteProjectType] tasks. This will then be referenced
 * from the various Write*Chart tasks to identify and customize nodes in the chart.
 */
@CacheableTask
public abstract class CollateProjectTypes : DefaultTask(), TaskWithOutputFile {
  @get:[PathSensitive(NONE) InputFiles] public abstract val projectTypeFiles: ConfigurableFileCollection
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = ATLAS_TASK_GROUP
    description = "Collates the calculated types of all projects in the project"
  }

  @TaskAction
  public fun execute() {
    val outputFile = outputFile.get().asFile
    val typedProjects = projectTypeFiles
      .filter { it.exists() }
      .map(::readProjectType)
      .toSortedSet()

    writeProjectTypes(
      projects = typedProjects,
      outputFile = outputFile,
    )

    logger.info("CollateProjectTypes: ${typedProjects.size} projects")
    typedProjects.forEach { typedProject ->
      logger.info("CollateProjectTypes:     typedProject=$typedProject")
    }
  }

  @InternalAtlasApi
  public companion object {
    private const val NAME = "collateProjectTypes"

    @InternalAtlasApi
    public fun get(target: Project): TaskProvider<CollateProjectTypes> =
      target.tasks.named(NAME, CollateProjectTypes::class.java)

    @InternalAtlasApi
    public fun register(target: Project): TaskProvider<CollateProjectTypes> = with(target) {
      tasks.register(NAME, CollateProjectTypes::class.java) { task ->
        task.outputFile.convention(fileInBuildDirectory("project-types.json"))
      }
    }
  }
}
