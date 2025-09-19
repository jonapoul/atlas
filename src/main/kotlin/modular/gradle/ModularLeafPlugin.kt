/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.gradle

import modular.graphviz.internal.registerGraphVizLeafTasks
import modular.graphviz.spec.GraphVizSpec
import modular.internal.ModularExtensionImpl
import modular.internal.Variant
import modular.internal.configureSeparators
import modular.internal.outputFile
import modular.internal.registerModularGenerateTask
import modular.mermaid.internal.registerMermaidLeafTasks
import modular.mermaid.spec.MermaidSpec
import modular.tasks.CalculateModuleTreeTask
import modular.tasks.DumpModuleLinksTask
import modular.tasks.DumpModuleTypeTask
import modular.tasks.MODULAR_TASK_GROUP
import modular.tasks.registerGenerationTaskOnSync
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

    val generateModules = tasks.register("generateModules") { t ->
      t.group = MODULAR_TASK_GROUP
      t.description = "Wrapper task for the other 'generateModulesX' tasks"
    }

    extension.specs.configureEach { spec ->
      val file = outputFile(extension.outputs, Variant.Chart, fileExtension = spec.fileExtension.get())
      when (spec) {
        is GraphVizSpec -> registerGraphVizLeafTasks(extension, spec, file, generateModules)
        is MermaidSpec -> registerMermaidLeafTasks(extension, spec, file, generateModules)
        else -> logger.warn("Not sure how to handle spec: ${spec.javaClass.canonicalName}")
      }
    }
  }
}
