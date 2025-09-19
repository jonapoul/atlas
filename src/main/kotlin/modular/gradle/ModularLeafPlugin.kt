/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.gradle

import modular.core.internal.ModularExtensionImpl
import modular.core.internal.Variant
import modular.core.internal.configureSeparators
import modular.core.internal.outputFile
import modular.core.internal.registerModularGenerateTask
import modular.core.tasks.CalculateModuleTreeTask
import modular.core.tasks.DumpModuleLinksTask
import modular.core.tasks.DumpModuleTypeTask
import modular.core.tasks.MODULAR_TASK_GROUP
import modular.core.tasks.registerGenerationTaskOnSync
import modular.graphviz.internal.GraphVizSpecImpl
import modular.graphviz.internal.registerGraphVizLeafTasks
import modular.mermaid.internal.MermaidSpecImpl
import modular.mermaid.internal.registerMermaidLeafTasks
import org.gradle.api.Plugin
import org.gradle.api.Project

class ModularLeafPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    require(target != rootProject) {
      error("ModularLeafPlugin is only meant to be applied on a non-root project!")
    }

    val extension = rootProject.extensions.getByType(ModularExtension::class.java) as ModularExtensionImpl

    configureSeparators(extension)
    registerGenerationTaskOnSync(extension)
    registerModularGenerateTask()

    DumpModuleTypeTask.register(project, extension)
    DumpModuleLinksTask.register(project, extension)
    CalculateModuleTreeTask.register(project, extension)

    extension.specs.configureEach { spec ->
      val file = outputFile(extension.outputs, Variant.Chart, fileExtension = spec.fileExtension.get())
      when (spec) {
        is GraphVizSpecImpl -> registerGraphVizLeafTasks(extension, spec, file)
        is MermaidSpecImpl -> registerMermaidLeafTasks(extension, spec, file)
        else -> logger.warn("Not sure how to handle spec: ${spec.javaClass.canonicalName}")
      }
    }
  }
}
