/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import modular.gradle.ModularExtension
import modular.internal.MODULAR_TASK_GROUP
import modular.internal.ModuleLink
import modular.internal.ModuleLinks
import modular.internal.fileInReportDirectory
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
abstract class CalculateModuleTreeTask : DefaultTask(), TaskWithSeparator, TaskWithOutputFile {
  @get:[PathSensitive(RELATIVE) InputFile] abstract val collatedLinks: RegularFileProperty
  @get:Input abstract val supportUpwardsTraversal: Property<Boolean>
  @get:Input abstract val thisPath: Property<String>
  @get:Input abstract override val separator: Property<String>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    description = "Generates the tree of module links"
  }

  @TaskAction
  fun execute() {
    val thisPath = thisPath.get()
    val separator = separator.get()

    val allLinks = ModuleLinks.read(collatedLinks.get().asFile, separator)
    val tree = mutableSetOf<ModuleLink>()

    if (supportUpwardsTraversal.get()) {
      calculate(thisPath, Direction.Up, allLinks, tree)
    }
    calculate(thisPath, Direction.Down, allLinks, tree)

    val outputFile = outputFile.get().asFile
    ModuleLinks.write(tree, outputFile, separator)

    logger.info("CalculateModuleTreeTask: written ${tree.size} links from ${allLinks.size} across the project")
    tree.forEach { (from, to, config) ->
      logger.info("CalculateModuleTreeTask:     from=$from, to=$to, config=$config")
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

  companion object {
    private const val NAME = "calculateModuleTree"

    fun get(target: Project): TaskProvider<CalculateModuleTreeTask> =
      target.tasks.named(NAME, CalculateModuleTreeTask::class.java)

    fun register(
      target: Project,
      extension: ModularExtension,
    ): TaskProvider<CalculateModuleTreeTask> = with(target) {
      val calculateTree = tasks.register(NAME, CalculateModuleTreeTask::class.java) { task ->
        task.thisPath.set(target.path)
        task.supportUpwardsTraversal.set(extension.supportUpwardsTraversal)
        task.outputFile.set(fileInReportDirectory("module-tree"))
      }

      gradle.projectsEvaluated {
        val collateProjectLinks = CollateModuleLinksTask.get(rootProject)
        calculateTree.configure { t ->
          t.collatedLinks.set(collateProjectLinks.map { it.outputFile.get() })
          t.dependsOn(collateProjectLinks)
        }
      }

      calculateTree
    }
  }
}
