/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.gradle

import modular.core.internal.ModularExtensionImpl
import modular.core.internal.configureSeparators
import modular.core.internal.orderedTypes
import modular.core.internal.registerModularGenerateTask
import modular.core.internal.warnIfModuleTypesSpecifyNothing
import modular.core.internal.warnIfNoGraphVizOutputs
import modular.core.internal.warnIfSvgSelectedWithCustomDpi
import modular.core.tasks.CollateModuleLinksTask
import modular.core.tasks.CollateModuleTypesTask
import modular.core.tasks.registerGenerationTaskOnSync
import modular.graphviz.internal.GraphVizSpecImpl
import modular.graphviz.internal.registerGraphVizTrunkTasks
import modular.mermaid.internal.MermaidSpecImpl
import modular.mermaid.internal.registerMermaidTrunkTasks
import org.gradle.api.Plugin
import org.gradle.api.Project

class ModularTrunkPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    require(target == rootProject) {
      error("ModularTrunkPlugin should only be applied on the root project - you applied it to $path")
    }

    val extension = extensions.create(
      ModularExtension::class.java,
      ModularExtensionImpl.NAME,
      ModularExtensionImpl::class.java,
    ) as ModularExtensionImpl

    configureSeparators(extension)
    registerGenerationTaskOnSync(extension)
    registerModularGenerateTask()

    CollateModuleTypesTask.register(project)
    CollateModuleLinksTask.register(project, extension)

    extension.specs.configureEach { spec ->
      when (spec) {
        is GraphVizSpecImpl -> registerGraphVizTrunkTasks(extension, spec)
        is MermaidSpecImpl -> registerMermaidTrunkTasks(extension, spec)
        else -> logger.warn("Not sure how to handle spec: ${spec.javaClass.canonicalName}")
      }
    }

    afterEvaluate {
      warnIfSvgSelectedWithCustomDpi(extension)
      warnIfNoGraphVizOutputs(extension)

      val types = extension.orderedTypes()
      warnIfModuleTypesSpecifyNothing(types)
    }
  }
}
