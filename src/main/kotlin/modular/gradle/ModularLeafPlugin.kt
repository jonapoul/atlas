/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.gradle

import modular.core.internal.ModularExtensionImpl
import modular.core.internal.configurePrintFilesToConsole
import modular.core.internal.configureSeparators
import modular.core.internal.registerModularGenerateTask
import modular.core.tasks.WriteModuleLinks
import modular.core.tasks.WriteModuleTree
import modular.core.tasks.WriteModuleType
import modular.core.tasks.registerGenerationTaskOnSync
import modular.graphviz.internal.GraphvizSpecImpl
import modular.graphviz.internal.registerGraphvizLeafTasks
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
    configurePrintFilesToConsole(extension)
    registerGenerationTaskOnSync(extension)
    registerModularGenerateTask()

    WriteModuleType.register(project, extension)
    WriteModuleLinks.register(project, extension)
    WriteModuleTree.register(project, extension)

    extension.specs.configureEach { spec ->
      when (spec) {
        is GraphvizSpecImpl -> registerGraphvizLeafTasks(extension, spec)
        is MermaidSpecImpl -> registerMermaidLeafTasks(extension, spec)
        else -> logger.warn("Not sure how to handle spec: ${spec.javaClass.canonicalName}")
      }
    }
  }
}
