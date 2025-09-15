/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.gradle

import modular.graphviz.internal.registerGraphVizTrunkTasks
import modular.graphviz.spec.GraphVizSpec
import modular.internal.ModularExtensionImpl
import modular.internal.configureSeparators
import modular.internal.orderedTypes
import modular.internal.registerModularGenerateTask
import modular.internal.warnIfModuleTypesSpecifyNothing
import modular.internal.warnIfNoGraphVizOutputs
import modular.internal.warnIfSvgSelectedWithCustomDpi
import modular.mermaid.internal.registerMermaidTrunkTasks
import modular.mermaid.spec.MermaidSpec
import modular.tasks.CollateModuleLinksTask
import modular.tasks.CollateModuleTypesTask
import modular.tasks.MODULAR_TASK_GROUP
import modular.tasks.registerGenerationTaskOnSync
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

    val generateLegend = tasks.register("generateLegend") { t ->
      t.group = MODULAR_TASK_GROUP
      t.description = "Wrapper task for the other 'generateLegendX' tasks"
    }

    extension.specs.configureEach { spec ->
      when (spec) {
        is GraphVizSpec -> registerGraphVizTrunkTasks(extension, spec, generateLegend)
        is MermaidSpec -> registerMermaidTrunkTasks(extension, spec, generateLegend)
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
