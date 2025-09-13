/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.gradle

import modular.graphviz.spec.GraphVizSpec
import modular.graphviz.tasks.CheckDotFileTask
import modular.graphviz.tasks.GenerateGraphvizFileTask
import modular.graphviz.tasks.GenerateLegendDotFileTask
import modular.internal.ModularExtensionImpl
import modular.internal.Variant
import modular.internal.configureSeparators
import modular.internal.modularBuildDirectory
import modular.internal.orderedTypes
import modular.internal.outputFile
import modular.internal.registerGenerationTaskOnSync
import modular.internal.warnIfModuleTypesSpecifyNothing
import modular.internal.warnIfNoGraphVizOutputs
import modular.internal.warnIfNoModuleTypes
import modular.internal.warnIfSvgSelectedWithCustomDpi
import modular.tasks.CollateModuleLinksTask
import modular.tasks.CollateModuleTypesTask
import modular.tasks.MODULAR_TASK_GROUP
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider

class ModularTrunkPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    if (target != rootProject) {
      error("ModularTrunkPlugin should only be applied on the root project - you applied it to $path")
    }

    val extension = extensions.create(
      ModularExtension::class.java,
      ModularExtensionImpl.NAME,
      ModularExtensionImpl::class.java,
    ) as ModularExtensionImpl

    configureSeparators(extension)
    registerGenerationTaskOnSync(extension)

    CollateModuleTypesTask.register(project)
    CollateModuleLinksTask.register(project, extension)

    val generateLegend = tasks.register("generateLegend") { t ->
      t.group = MODULAR_TASK_GROUP
      t.description = "Wrapper task for the other 'generateLegendX' tasks"
    }

    extension.specs.configureEach { spec ->
      when (spec) {
        is GraphVizSpec -> registerGraphVizTasks(extension, spec, generateLegend)
        else -> logger.warn("Not sure how to handle spec: ${spec.javaClass.canonicalName}")
      }
    }

    afterEvaluate {
      warnIfSvgSelectedWithCustomDpi(extension)
      warnIfNoGraphVizOutputs(extension)

      val types = extension.orderedTypes()
      warnIfNoModuleTypes(types)
      warnIfModuleTypesSpecifyNothing(types)
    }
  }

  private fun Project.registerGraphVizTasks(
    extension: ModularExtensionImpl,
    spec: GraphVizSpec,
    generateLegend: TaskProvider<Task>,
  ) {
    val legend = spec.legend
    if (legend != null) {
      // Only create a legend if one of the legend functions was explicitly called
      val outputFile = GenerateLegendDotFileTask.defaultOutputFile(extension, spec)
      val dotFileTask = GenerateLegendDotFileTask.register(
        target = this,
        name = GenerateLegendDotFileTask.TASK_NAME,
        legendSpec = legend,
        extension = extension,
        outputFile = outputFile,
      )
      val graphVizTasks = GenerateGraphvizFileTask.register(this, extension, spec, Variant.Legend, dotFileTask)
      generateLegend.configure { it.dependsOn(graphVizTasks) }

      // Also validate the legend's dotfile when we call gradle check
      val tempTask = GenerateLegendDotFileTask.register(
        target = this,
        name = GenerateLegendDotFileTask.TASK_NAME_FOR_CHECKING,
        legendSpec = legend,
        extension = extension,
        outputFile = modularBuildDirectory.map { it.file("legend-temp.dot") },
      )

      val checkTask = CheckDotFileTask.register(
        target = this,
        name = CheckDotFileTask.NAME_LEGEND,
        generateDotFile = tempTask,
        realDotFile = outputFile(extension.outputs, Variant.Legend, fileExtension = spec.fileExtension.get()),
      )
      tasks.maybeCreate("check").dependsOn(checkTask)
    }
  }
}
