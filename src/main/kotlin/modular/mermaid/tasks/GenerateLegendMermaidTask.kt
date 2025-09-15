/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.mermaid.tasks

import modular.graphviz.spec.GraphVizLegendSpec
import modular.graphviz.spec.GraphVizSpec
import modular.internal.ModularExtensionImpl
import modular.internal.buildIndentedString
import modular.internal.moduleTypeModel
import modular.internal.orderedTypes
import modular.mermaid.spec.MermaidLegendSpec
import modular.mermaid.spec.MermaidSpec
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
abstract class GenerateLegendMermaidTask : DefaultTask(), TaskWithSeparator, ModularGenerationTask, TaskWithOutputFile {
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

    val mermaidContents = buildIndentedString {
      appendLine("TBC")
    }

    outputFile.writeText(mermaidContents)

    logger.lifecycle("Written ${mermaidContents.length} chars to $outputFile")
  }

  companion object {
    internal const val TASK_NAME: String = "generateLegendMermaid"
    internal const val TASK_NAME_FOR_CHECKING: String = "generateLegendMermaidForChecking"

    fun get(target: Project): TaskProvider<GenerateLegendMermaidTask> =
      target.tasks.named(TASK_NAME, GenerateLegendMermaidTask::class.java)

    internal fun register(
      target: Project,
      name: String,
      legendSpec: MermaidLegendSpec,
      extension: ModularExtensionImpl,
      outputFile: Provider<RegularFile>,
    ): TaskProvider<GenerateLegendMermaidTask> = with(target) {
      tasks.register(name, GenerateLegendMermaidTask::class.java) { task ->
        task.outputFile.convention(outputFile)
        task.moduleTypes.convention(extension.orderedTypes().map(::moduleTypeModel))
        task.linkTypes.convention(extension.linkTypes.linkTypes)
      }
    }
  }
}
