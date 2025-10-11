/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.tasks

import modular.core.LinkType
import modular.core.ModuleType
import modular.core.internal.DummyModularGenerationTask
import modular.core.internal.MODULAR_TASK_GROUP
import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant.Legend
import modular.core.internal.logIfConfigured
import modular.core.internal.modularBuildDirectory
import modular.core.internal.moduleType
import modular.core.internal.orderedLinkTypes
import modular.core.internal.orderedModuleTypes
import modular.core.internal.outputFile
import modular.core.internal.qualifier
import modular.core.tasks.ModularGenerationTask
import modular.core.tasks.TaskWithOutputFile
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.work.DisableCachingByDefault
import java.io.File

@CacheableTask
public abstract class WriteMarkdownLegend : DefaultTask(), TaskWithOutputFile, ModularGenerationTask {
  @get:Input public abstract val moduleTypes: ListProperty<ModuleType>
  @get:Input public abstract val linkTypes: SetProperty<LinkType>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    description = "Generates the legend for a project dependency graph"
  }

  @TaskAction
  public open fun execute() {
    val moduleTypes = moduleTypes.get()
    val linkTypes = linkTypes.get()
    val outputFile = outputFile.get().asFile

    val hasModuleTypes = moduleTypes.isNotEmpty()
    val hasLinkTypes = linkTypes.isNotEmpty()

    val contents = buildString {
      if (hasModuleTypes) {
        appendModuleTypesTable(moduleTypes)
      }

      if (hasLinkTypes) {
        if (hasModuleTypes) appendLine()
        appendLinkTypesTable(linkTypes)
      }
    }

    outputFile.writeText(contents)
    logIfConfigured(outputFile)
  }

  private fun StringBuilder.appendModuleTypesTable(moduleTypes: List<ModuleType>) {
    appendLine("| Module Types | Color |")
    appendLine("|:--:|:--:|")

    for (type in moduleTypes) {
      val url = "https://img.shields.io/badge/-%20-${parseColor(type.color)}?style=flat-square"
      val img = "<img src=\"$url\" height=\"30\" width=\"100\">"
      appendLine("| ${type.name} | $img |")
    }
  }

  private fun StringBuilder.appendLinkTypesTable(linkTypes: Set<LinkType>) {
    appendLine("| Link Types | Style |")
    appendLine("|:--:|:--:|")

    for (type in linkTypes) {
      val style = listOfNotNull(type.color, type.style).joinToString(separator = " ") { it.capitalized() }
      appendLine("| ${type.displayName} | $style |")
    }
  }

  private fun parseColor(color: String) = if (color.matches("#[0-9A-Fa-f]{6}".toRegex())) {
    // hex color, e.g. "#ABC123" -> "ABC123"
    color.removePrefix("#")
  } else {
    // assume it's a valid color name, e.g. "orange"
    color
  }

  @DisableCachingByDefault
  internal abstract class WriteMarkdownLegendDummy : WriteMarkdownLegend(), DummyModularGenerationTask

  internal companion object {
    private const val TASK_NAME = "writeMermaidLegend"

    internal fun get(target: Project): TaskProvider<WriteMarkdownLegend> =
      target.tasks.named(TASK_NAME, WriteMarkdownLegend::class.java)

    internal fun real(
      target: Project,
      extension: ModularExtensionImpl,
    ) = register<WriteMarkdownLegend>(
      target,
      extension,
      outputFile = target.outputFile(Legend, fileExtension = "md"),
    )

    internal fun dummy(
      target: Project,
      extension: ModularExtensionImpl,
    ) = register<WriteMarkdownLegendDummy>(
      target = target,
      extension = extension,
      outputFile = target.modularBuildDirectory
        .get()
        .file("legend-temp.md")
        .asFile,
    )

    private inline fun <reified T : WriteMarkdownLegend> register(
      target: Project,
      extension: ModularExtensionImpl,
      outputFile: File,
    ): TaskProvider<T> = with(target) {
      val name = "write${T::class.qualifier}MermaidLegend"
      val writeLegend = tasks.register(name, T::class.java) { task ->
        task.outputFile.set(outputFile)
      }

      writeLegend.configure { task ->
        task.moduleTypes.convention(extension.orderedModuleTypes().map(::moduleType))
        task.linkTypes.convention(extension.orderedLinkTypes())
      }

      return writeLegend
    }
  }
}
