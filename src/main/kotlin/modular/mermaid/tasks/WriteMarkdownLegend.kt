/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.tasks

import modular.core.internal.ModularExtensionImpl
import modular.core.internal.moduleTypeModel
import modular.core.internal.orderedTypes
import modular.core.spec.LinkType
import modular.core.spec.ModuleTypeModel
import modular.core.tasks.MODULAR_TASK_GROUP
import modular.core.tasks.ModularGenerationTask
import modular.core.tasks.TaskWithOutputFile
import modular.core.tasks.TaskWithSeparator
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider

@CacheableTask
abstract class WriteMarkdownLegend :
  DefaultTask(),
  TaskWithSeparator,
  ModularGenerationTask,
  TaskWithOutputFile {
  @get:Input abstract override val separator: Property<String>
  @get:Input abstract val moduleTypes: ListProperty<ModuleTypeModel>
  @get:Input abstract val linkTypes: SetProperty<LinkType>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    description = "Generates the legend for a project dependency graph"
  }

  @TaskAction
  fun execute() {
    val moduleTypes = moduleTypes.get()
    val linkTypes = linkTypes.get()
    val outputFile = outputFile.get().asFile

    val hasModuleTypes = moduleTypes.isNotEmpty()
    val hasLinkTypes = linkTypes.isNotEmpty()

    val contents = buildString {
      if (hasModuleTypes) {
        appendModuleTypesTable(moduleTypes)
        appendLine()
      }

      if (hasLinkTypes) {
        appendLinkTypesTable(linkTypes)
        appendLine()
      }
    }

    outputFile.writeText(contents)
    logIfConfigured(outputFile)
  }

  private fun StringBuilder.appendModuleTypesTable(moduleTypes: List<ModuleTypeModel>) {
    appendLine("| Module Types | Color |")
    appendLine("|:--:|:--:|")

    for (type in moduleTypes) {
      val url = "https://img.shields.io/badge/-%20-${colorToHex(type.color)}?style=flat-square"
      val img = "<img src=\"$url\" height=\"30\" width=\"100\">"
      appendLine("| ${type.name} | $img |")
    }
  }

  private fun StringBuilder.appendLinkTypesTable(linkTypes: Set<LinkType>) {
    appendLine("| Link Types | Style |")
    appendLine("|:--:|:--:|")

    for (type in linkTypes) {
      // TODO https://github.com/jonapoul/modular/issues/119
      val style = listOfNotNull(type.color, type.style).joinToString(separator = " ")
      appendLine("| ${type.configuration} | $style |")
    }
  }

  private fun colorToHex(color: String): String {
    if (color.matches("#[0-9A-Fa-f]{6}".toRegex())) {
      return color.removePrefix("#")
    }

    // TODO https://github.com/jonapoul/modular/issues/118
    error("Don't currently support non-hex colors, received '$color'")
  }

  internal companion object {
    internal const val TASK_NAME: String = "generateLegendMarkdown"
    internal const val TASK_NAME_FOR_CHECKING: String = "generateLegendMarkdownForChecking"

    internal fun get(target: Project): TaskProvider<WriteMarkdownLegend> =
      target.tasks.named(TASK_NAME, WriteMarkdownLegend::class.java)

    internal fun register(
      target: Project,
      name: String,
      extension: ModularExtensionImpl,
      outputFile: Provider<RegularFile>,
    ): TaskProvider<WriteMarkdownLegend> = with(target) {
      tasks.register(name, WriteMarkdownLegend::class.java) { task ->
        task.outputFile.convention(outputFile)
        task.moduleTypes.convention(extension.orderedTypes().map(::moduleTypeModel))
        task.linkTypes.convention(extension.linkTypes.linkTypes)
      }
    }
  }
}
