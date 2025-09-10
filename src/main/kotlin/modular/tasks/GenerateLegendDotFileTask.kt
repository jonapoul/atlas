/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.tasks

import modular.gradle.ModularExtension
import modular.internal.MODULAR_TASK_GROUP
import modular.internal.appendIndentedLine
import modular.internal.moduleTypeModel
import modular.internal.orderedTypes
import modular.spec.DotFileLegendSpec
import modular.spec.ModuleTypeModel
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

@CacheableTask
abstract class GenerateLegendDotFileTask : DefaultTask(), TaskWithSeparator, ModularGenerationTask {
  @get:Input abstract val tableBorder: Property<Int>
  @get:Input abstract val cellBorder: Property<Int>
  @get:Input abstract val cellSpacing: Property<Int>
  @get:Input abstract val cellPadding: Property<Int>
  @get:Input abstract override val separator: Property<String>
  @get:Input abstract val moduleTypes: ListProperty<ModuleTypeModel>
  @get:OutputFile abstract val dotFile: RegularFileProperty

  init {
    group = MODULAR_TASK_GROUP
    description = "Generates the legend for a project dependency graph"
  }

  @TaskAction
  fun execute() {
    val tb = tableBorder.get()
    val cb = cellBorder.get()
    val cs = cellSpacing.get()
    val cp = cellPadding.get()
    val moduleTypes = moduleTypes.get()

    val dotFileContents = buildString {
      appendLine("digraph {")
      appendIndentedLine("node [shape=plaintext]")
      appendIndentedLine("table1 [label=<")
      appendIndentedLine("<TABLE BORDER=\"$tb\" CELLBORDER=\"$cb\" CELLSPACING=\"$cs\" CELLPADDING=\"$cp\">")
      moduleTypes.forEach { type ->
        appendIndentedLine("<TR><TD>${type.name}</TD><TD BGCOLOR=\"${type.color}\">module-name</TD></TR>")
      }
      appendIndentedLine("</TABLE>")
      appendIndentedLine(">];")
      appendLine("}")
    }

    dotFile.get().asFile.writeText(dotFileContents)

    logger.lifecycle("Written ${dotFileContents.length} chars to ${dotFile.get().asFile}")
  }

  companion object {
    const val TASK_NAME: String = "generateLegendDotFile"

    fun get(target: Project): TaskProvider<GenerateLegendDotFileTask> =
      target.tasks.named(TASK_NAME, GenerateLegendDotFileTask::class.java)

    fun register(
      target: Project,
      spec: DotFileLegendSpec,
      extension: ModularExtension,
    ): TaskProvider<GenerateLegendDotFileTask> = with(target) {
      tasks.register(TASK_NAME, GenerateLegendDotFileTask::class.java) { task ->
        task.tableBorder.set(spec.tableBorder)
        task.cellBorder.set(spec.cellBorder)
        task.cellSpacing.set(spec.cellSpacing)
        task.cellPadding.set(spec.cellPadding)
        task.dotFile.set(spec.file)
        task.moduleTypes.set(extension.orderedTypes().map(::moduleTypeModel))
      }
    }
  }
}
