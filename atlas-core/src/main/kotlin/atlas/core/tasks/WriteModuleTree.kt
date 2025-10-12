/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.core.tasks

import atlas.core.AtlasExtension
import atlas.core.InternalAtlasApi
import atlas.core.internal.ATLAS_TASK_GROUP
import atlas.core.internal.ModuleLink
import atlas.core.internal.fileInBuildDirectory
import atlas.core.internal.readModuleLinks
import atlas.core.internal.writeModuleLinks
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity.RELATIVE
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

@CacheableTask
public abstract class WriteModuleTree : DefaultTask(), TaskWithOutputFile {
  @get:[PathSensitive(RELATIVE) InputFile] public abstract val collatedLinks: RegularFileProperty
  @get:Input public abstract val alsoTraverseUpwards: Property<Boolean>
  @get:Input public abstract val thisPath: Property<String>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = ATLAS_TASK_GROUP
    description = "Generates the tree of module links"
  }

  @TaskAction
  public fun execute() {
    val thisPath = thisPath.get()

    val allLinks = readModuleLinks(collatedLinks.get().asFile)
    val tree = mutableSetOf<ModuleLink>()

    if (alsoTraverseUpwards.get()) {
      calculate(thisPath, Direction.Up, allLinks, tree)
    }
    calculate(thisPath, Direction.Down, allLinks, tree)

    val outputFile = outputFile.get().asFile
    writeModuleLinks(tree, outputFile)

    logger.info("CalculateModuleTree: written ${tree.size} links from ${allLinks.size} across the project")
    tree.forEach { link ->
      logger.info("CalculateModuleTree:     link = $link")
    }
  }

  private enum class Direction { Up, Down }

  private fun calculate(
    targetPath: String,
    direction: Direction,
    allLinks: Set<ModuleLink>,
    output: MutableSet<ModuleLink>,
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
    private const val NAME = "writeModuleTree"

    @InternalAtlasApi
    public fun get(target: Project): TaskProvider<WriteModuleTree> =
      target.tasks.named(NAME, WriteModuleTree::class.java)

    @InternalAtlasApi
    public fun register(
      target: Project,
      extension: AtlasExtension,
    ): TaskProvider<WriteModuleTree> = with(target) {
      val calculateTree = tasks.register(NAME, WriteModuleTree::class.java) { task ->
        task.thisPath.convention(target.path)
        task.outputFile.convention(fileInBuildDirectory("module-tree.json"))
      }

      calculateTree.configure { task ->
        task.alsoTraverseUpwards.convention(extension.alsoTraverseUpwards)
      }

      gradle.projectsEvaluated {
        val collateProjectLinks = CollateModuleLinks.get(rootProject)
        calculateTree.configure { t ->
          t.collatedLinks.convention(collateProjectLinks.map { it.outputFile.get() })
          t.dependsOn(collateProjectLinks)
        }
      }

      calculateTree
    }
  }
}
