/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.tasks

import modular.internal.ModularExtensionImpl
import modular.internal.buildIndentedString
import modular.internal.moduleTypeModel
import modular.internal.orderedTypes
import modular.spec.LinkType
import modular.spec.ModuleTypeModel
import modular.tasks.MODULAR_TASK_GROUP
import modular.tasks.ModularGenerationTask
import modular.tasks.TaskWithOutputFile
import modular.tasks.TaskWithSeparator
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
abstract class GenerateLegendDotFileTask : DefaultTask(), TaskWithSeparator, ModularGenerationTask, TaskWithOutputFile {
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
            val style = type.style ?: "&lt;style&gt;"
            appendLine("  <TR><TD>${type.configuration}</TD><TD$bgColor>$style</TD></TR>")
          }
          appendLine("</TABLE>")
          appendLine(">];")
        }
      }

      appendLine("}")
    }

    outputFile.writeText(dotFileContents)

    logger.lifecycle("Written ${dotFileContents.length} chars to $outputFile")
  }

  internal companion object {
    internal const val TASK_NAME: String = "generateLegendDotFile"
    internal const val TASK_NAME_FOR_CHECKING: String = "generateLegendDotFileForChecking"

    internal fun get(target: Project): TaskProvider<GenerateLegendDotFileTask> =
      target.tasks.named(TASK_NAME, GenerateLegendDotFileTask::class.java)

    internal fun register(
      target: Project,
      name: String,
      extension: ModularExtensionImpl,
      outputFile: Provider<RegularFile>,
    ): TaskProvider<GenerateLegendDotFileTask> = with(target) {
      tasks.register(name, GenerateLegendDotFileTask::class.java) { task ->
        task.outputFile.convention(outputFile)
        task.moduleTypes.convention(extension.orderedTypes().map(::moduleTypeModel))
        task.linkTypes.convention(extension.linkTypes.linkTypes)
      }
    }
  }
}
