package atlas.core.tasks

import atlas.core.LinkType
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.AtlasExtensionImpl
import atlas.core.internal.createProjectLinks
import atlas.core.internal.fileInBuildDirectory
import atlas.core.internal.orderedLinkTypes
import atlas.core.internal.writeProjectLinks
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.UnknownTaskException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

/**
 * Dumps all [atlas.core.internal.ProjectLink]s between this and any other projects to a file.
 */
@CacheableTask
public abstract class WriteProjectLinks : DefaultTask(), TaskWithOutputFile {
  @get:Input public abstract val projectLinks: MapProperty<String, List<String>>
  @get:Input public abstract val linkTypes: SetProperty<LinkType>
  @get:Input public abstract val thisPath: Property<String>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = ATLAS_TASK_GROUP
    description = "Finds links between this project and its children"
  }

  @TaskAction
  public fun execute() {
    val links = writeProjectLinks(
      outputFile = outputFile.get().asFile,
      fromPath = thisPath.get(),
      projectLinks = projectLinks.get(),
      linkTypes = linkTypes.get(),
    )

    logger.info("WriteProjectLinks: ${links.size} links")
    links.forEach {
      logger.info("WriteProjectLinks:     link=$it")
    }
  }

  internal companion object {
    private const val NAME = "writeProjectLinks"

    internal fun get(target: Project): TaskProvider<WriteProjectLinks>? = try {
      target.tasks.named(NAME, WriteProjectLinks::class.java)
    } catch (_: UnknownTaskException) {
      null
    }

    internal fun register(
      target: Project,
      extension: AtlasExtensionImpl,
    ): TaskProvider<WriteProjectLinks> = with(target) {
      val writeLinks = tasks.register(NAME, WriteProjectLinks::class.java) { task ->
        task.thisPath.convention(target.path)
        task.outputFile.convention(fileInBuildDirectory("project-links.json"))
      }

      writeLinks.configure { task ->
        task.projectLinks.convention(createProjectLinks(target, extension.ignoredConfigs.get()))
        task.linkTypes.convention(extension.orderedLinkTypes())
      }

      return writeLinks
    }
  }
}
