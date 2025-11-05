package atlas.core.tasks

import atlas.core.AtlasExtension
import atlas.core.InternalAtlasApi
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.ProjectLink
import atlas.core.internal.fileInBuildDirectory
import atlas.core.internal.readProjectLinks
import atlas.core.internal.writeProjectLinks
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.NONE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

/**
 * Gathers a snapshot of the whole-project [collatedLinks] data to only find those projects and links relevant to
 * [thisPath]. If [alsoTraverseUpwards] is enabled, upstream projects will be included too.
 */
@CacheableTask
public abstract class WriteProjectTree : DefaultTask(), TaskWithOutputFile {
  @get:[PathSensitive(NONE) InputFile] public abstract val collatedLinks: RegularFileProperty
  @get:Input public abstract val alsoTraverseUpwards: Property<Boolean>
  @get:Input public abstract val thisPath: Property<String>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = ATLAS_TASK_GROUP
    description = "Generates the tree of project links"
  }

  @TaskAction
  public fun execute() {
    val thisPath = thisPath.get()

    val allLinks = readProjectLinks(collatedLinks.get().asFile)
    val tree = mutableSetOf<ProjectLink>()

    if (alsoTraverseUpwards.get()) {
      calculate(thisPath, Direction.Up, allLinks, tree)
    }
    calculate(thisPath, Direction.Down, allLinks, tree)

    val outputFile = outputFile.get().asFile
    writeProjectLinks(tree, outputFile)

    logger.info("WriteProjectTree: written ${tree.size} links from ${allLinks.size} across the project")
    tree.forEach { link ->
      logger.info("WriteProjectTree:     link = $link")
    }
  }

  private enum class Direction { Up, Down }

  private fun calculate(
    targetPath: String,
    direction: Direction,
    allLinks: Set<ProjectLink>,
    output: MutableSet<ProjectLink>,
  ) {
    when (direction) {
      Direction.Up -> {
        val relevantLinks = allLinks.filter { it.toPath == targetPath }
        output += relevantLinks
        for (link in relevantLinks) {
          calculate(link.fromPath, direction, allLinks, output)
        }
      }

      Direction.Down -> {
        val relevantLinks = allLinks.filter { it.fromPath == targetPath }
        output += relevantLinks
        for (link in relevantLinks) {
          calculate(link.toPath, direction, allLinks, output)
        }
      }
    }
  }

  @InternalAtlasApi
  public companion object {
    private const val NAME = "writeProjectTree"

    @InternalAtlasApi
    public fun get(target: Project): TaskProvider<WriteProjectTree> =
      target.tasks.named(NAME, WriteProjectTree::class.java)

    @InternalAtlasApi
    public fun register(
      target: Project,
      extension: AtlasExtension,
    ): TaskProvider<WriteProjectTree> = with(target) {
      tasks.register(NAME, WriteProjectTree::class.java) { task ->
        task.thisPath.convention(target.path)
        task.outputFile.convention(fileInBuildDirectory("project-tree.json"))
        task.alsoTraverseUpwards.convention(extension.alsoTraverseUpwards)
      }
    }
  }
}
