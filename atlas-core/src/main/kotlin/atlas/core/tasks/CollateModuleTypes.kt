package atlas.core.tasks

import atlas.core.InternalAtlasApi
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.fileInBuildDirectory
import atlas.core.internal.readModuleType
import atlas.core.internal.writeModuleTypes
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
 * Registered on the root project to aggregate the results of [WriteModuleType] tasks. This will then be referenced
 * from the various Write*Chart tasks to identify and customize nodes in the chart.
 */
@CacheableTask
public abstract class CollateModuleTypes : DefaultTask(), TaskWithOutputFile {
  @get:[PathSensitive(NONE) InputFiles] public abstract val projectTypeFiles: ConfigurableFileCollection
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = ATLAS_TASK_GROUP
    description = "Collates the calculated types of all modules in the project"
  }

  @TaskAction
  public fun execute() {
    val outputFile = outputFile.get().asFile
    val typedModules = projectTypeFiles
      .filter { it.exists() }
      .map(::readModuleType)
      .toSortedSet()

    writeModuleTypes(
      modules = typedModules,
      outputFile = outputFile,
    )

    logger.info("CollateModuleTypes: ${typedModules.size} modules")
    typedModules.forEach { typedModule ->
      logger.info("CollateModuleTypes:     typedModule=$typedModule")
    }
  }

  @InternalAtlasApi
  public companion object {
    private const val NAME = "collateModuleTypes"

    @InternalAtlasApi
    public fun get(target: Project): TaskProvider<CollateModuleTypes> =
      target.tasks.named(NAME, CollateModuleTypes::class.java)

    @InternalAtlasApi
    public fun register(target: Project): TaskProvider<CollateModuleTypes> = with(target) {
      tasks.register(NAME, CollateModuleTypes::class.java) { task ->
        task.outputFile.convention(fileInBuildDirectory("module-types.json"))
      }
    }
  }
}
