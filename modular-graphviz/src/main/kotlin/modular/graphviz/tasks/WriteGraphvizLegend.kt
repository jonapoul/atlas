/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.graphviz.tasks

import modular.core.LinkType
import modular.core.ModuleType
import modular.core.internal.DummyModularGenerationTask
import modular.core.internal.MODULAR_TASK_GROUP
import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant.Legend
import modular.core.internal.buildIndentedString
import modular.core.internal.logIfConfigured
import modular.core.internal.modularBuildDirectory
import modular.core.internal.moduleType
import modular.core.internal.orderedLinkTypes
import modular.core.internal.orderedModuleTypes
import modular.core.internal.outputFile
import modular.core.internal.qualifier
import modular.core.tasks.ModularGenerationTask
import modular.core.tasks.TaskWithOutputFile
import modular.graphviz.GraphvizSpec
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.work.DisableCachingByDefault
import java.io.File

@CacheableTask
abstract class WriteGraphvizLegend : DefaultTask(), TaskWithOutputFile, ModularGenerationTask {
  @get:Input abstract val moduleTypes: ListProperty<ModuleType>
  @get:Input abstract val linkTypes: ListProperty<LinkType>
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
            val style = type.style?.capitalized() ?: "Solid"
            appendLine("  <TR><TD>${type.displayName}</TD><TD$bgColor>$style</TD></TR>")
          }
          appendLine("</TABLE>")
          appendLine(">];")
        }
      }

      appendLine("}")
    }

    outputFile.writeText(dotFileContents)
    logIfConfigured(outputFile)
  }

  @DisableCachingByDefault
  internal abstract class WriteGraphvizLegendDummy : WriteGraphvizLegend(), DummyModularGenerationTask

  internal companion object {
    internal fun real(
      target: Project,
      spec: GraphvizSpec,
      extension: ModularExtensionImpl,
    ) = register<WriteGraphvizLegend>(
      target = target,
      extension = extension,
      outputFile = target.outputFile(Legend, spec.fileExtension.get()),
    )

    internal fun dummy(
      target: Project,
      extension: ModularExtensionImpl,
    ) = register<WriteGraphvizLegendDummy>(
      target = target,
      extension = extension,
      outputFile = target.modularBuildDirectory
        .get()
        .file("legend-temp.dot")
        .asFile,
    )

    internal inline fun <reified T : WriteGraphvizLegend> register(
      target: Project,
      extension: ModularExtensionImpl,
      outputFile: File,
    ): TaskProvider<T> = with(target) {
      val name = "write${T::class.qualifier}GraphvizLegend"
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
