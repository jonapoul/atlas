/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.tasks

import modular.core.InternalModularApi
import modular.core.internal.MODULAR_TASK_GROUP
import modular.core.internal.ModularExtensionImpl
import modular.core.internal.ModularGenerationTask
import modular.core.internal.TaskWithOutputFile
import modular.core.internal.TaskWithSeparator
import modular.core.internal.Variant
import modular.core.internal.buildIndentedString
import modular.core.internal.linkType
import modular.core.internal.logIfConfigured
import modular.core.internal.moduleType
import modular.core.internal.orderedLinkTypes
import modular.core.internal.orderedModuleTypes
import modular.core.spec.LinkType
import modular.core.spec.ModularSpec
import modular.core.spec.ModuleType
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.work.DisableCachingByDefault
import java.io.File

@CacheableTask
abstract class WriteGraphvizLegend : WriteGraphvizLegendBase(), ModularGenerationTask {
  override fun getDescription() = "Generates the legend for a project dependency graph"

  @TaskAction
  override fun execute() {
    super.execute()
    logIfConfigured(outputFile.get().asFile)
  }
}

@DisableCachingByDefault
abstract class WriteDummyGraphvizLegend : WriteGraphvizLegendBase() {
  override fun getDescription() = "Generates a dummy legend for comparison against the golden"

  @TaskAction
  override fun execute() = super.execute()
}

@CacheableTask
sealed class WriteGraphvizLegendBase : DefaultTask(), TaskWithSeparator, TaskWithOutputFile {
  @get:Input abstract override val separator: Property<String>
  @get:Input abstract val moduleTypes: ListProperty<ModuleType>
  @get:Input abstract val linkTypes: ListProperty<LinkType>
  @get:OutputFile abstract override val outputFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
  }

  abstract override fun getDescription(): String

  @TaskAction
  open fun execute() {
    val moduleTypes = moduleTypes.get()
    val linkTypes = linkTypes.get()
    val outputFile = outputFile.get().asFile

    val hasModuleTypes = moduleTypes.isNotEmpty()
    val hasLinkTypes = linkTypes.isNotEmpty()

    val dotFileContents = buildIndentedString {
      appendLine("digraph {")
      indent {
        appendLine("node [shape=plaintext]")

        if (hasModuleTypes) {
          appendLine("modules [label=<")
          appendLine("<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">")
          appendLine("  <TR><TD COLSPAN=\"2\" BGCOLOR=\"#DDDDDD\"><B>Module Types</B></TD></TR>")
          indent {
            moduleTypes.forEach { type ->
              appendLine("<TR><TD>${type.name}</TD><TD BGCOLOR=\"${type.color}\">&lt;module-name&gt;</TD></TR>")
            }
          }
          appendLine("</TABLE>")
          appendLine(">];")
        }

        if (hasLinkTypes) {
          appendLine("links [label=<")
          appendLine("<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">")
          appendLine("  <TR><TD COLSPAN=\"2\" BGCOLOR=\"#DDDDDD\"><B>Link Types</B></TD></TR>")
          linkTypes.forEach { type ->
            val bgColor = if (type.color == null) "" else " BGCOLOR=\"${type.color}\""
            val style = type.style ?: "Solid"
            appendLine("  <TR><TD>${type.displayName}</TD><TD$bgColor>${style.capitalized()}</TD></TR>")
          }
          appendLine("</TABLE>")
          appendLine(">];")
        }
      }

      appendLine("}")
    }

    outputFile.writeText(dotFileContents)
  }

  @InternalModularApi
  companion object {
    @InternalModularApi
    fun get(target: Project): TaskProvider<WriteGraphvizLegend> =
      target.tasks.named(TASK_NAME, WriteGraphvizLegend::class.java)

    @InternalModularApi
    inline fun <reified T : WriteGraphvizLegendBase> register(
      target: Project,
      variant: Variant,
      spec: ModularSpec,
      extension: ModularExtensionImpl,
      outputFile: File,
    ): TaskProvider<T> = with(target) {
      val qualifier = when (T::class) {
        WriteDummyGraphvizLegend::class -> "Dummy"
        else -> ""
      }
      val name = "write$qualifier${spec.name.capitalized()}$variant"
      tasks.register(name, T::class.java) { task ->
        task.outputFile.set(outputFile)
        task.moduleTypes.convention(extension.orderedModuleTypes().map(::moduleType))
        task.linkTypes.convention(extension.orderedLinkTypes().map(::linkType))
      }
    }
  }
}
