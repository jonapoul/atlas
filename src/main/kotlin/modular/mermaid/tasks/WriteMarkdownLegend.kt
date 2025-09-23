/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.tasks

import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant
import modular.core.internal.moduleTypeModel
import modular.core.internal.orderedTypes
import modular.core.spec.LinkType
import modular.core.spec.ModuleType
import modular.core.spec.Spec
import modular.core.tasks.MODULAR_TASK_GROUP
import modular.core.tasks.ModularGenerationTask
import modular.core.tasks.TaskWithOutputFile
import modular.core.tasks.TaskWithSeparator
import modular.core.tasks.logIfConfigured
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
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
abstract class WriteMarkdownLegend : WriteMarkdownLegendBase(), ModularGenerationTask {
  override fun getDescription() = "Generates the legend for a project dependency graph"

  @TaskAction
  override fun execute() {
    super.execute()
    logIfConfigured(outputFile.get().asFile)
  }
}

@DisableCachingByDefault
abstract class WriteDummyMarkdownLegend : WriteMarkdownLegendBase() {
  override fun getDescription() = "Generates a dummy legend for comparison against the golden"

  @TaskAction
  override fun execute() = super.execute()
}

@CacheableTask
sealed class WriteMarkdownLegendBase : DefaultTask(), TaskWithSeparator, TaskWithOutputFile {
  @get:Input abstract override val separator: Property<String>
  @get:Input abstract val moduleTypes: ListProperty<ModuleType>
  @get:Input abstract val linkTypes: SetProperty<LinkType>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    description = "Generates the legend for a project dependency graph"
  }

  @TaskAction
  open fun execute() {
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
  }

  private fun StringBuilder.appendModuleTypesTable(moduleTypes: List<ModuleType>) {
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
      val style = listOfNotNull(type.color, type.style).joinToString(separator = " ") { it.capitalized() }
      appendLine("| ${type.displayName} | $style |")
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
    internal fun get(target: Project): TaskProvider<WriteMarkdownLegendBase> =
      target.tasks.named(TASK_NAME, WriteMarkdownLegendBase::class.java)

    internal inline fun <reified T : WriteMarkdownLegendBase> register(
      target: Project,
      variant: Variant,
      spec: Spec,
      extension: ModularExtensionImpl,
      outputFile: File,
    ): TaskProvider<T> = with(target) {
      val qualifier = when (T::class) {
        WriteDummyMarkdownLegend::class -> "Dummy"
        else -> ""
      }
      val name = "write$qualifier${spec.name.capitalized()}$variant"
      tasks.register(name, T::class.java) { task ->
        task.outputFile.set(outputFile)
        task.moduleTypes.convention(extension.orderedTypes().map(::moduleTypeModel))
        task.linkTypes.convention(extension.linkTypes.linkTypes)
      }
    }
  }
}
