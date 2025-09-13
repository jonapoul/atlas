/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.gradle

import modular.internal.MODULAR_TASK_GROUP
import modular.internal.ModularExtensionImpl
import modular.internal.Variant
import modular.internal.configureSeparators
import modular.internal.failIfInvalidColors
import modular.internal.orderedTypes
import modular.internal.registerGenerationTaskOnSync
import modular.internal.warnIfModuleTypesSpecifyNothing
import modular.internal.warnIfNoModuleTypes
import modular.internal.warnIfSvgSelectedWithCustomDpi
import modular.spec.DotFileSpec
import modular.tasks.CollateModuleLinksTask
import modular.tasks.CollateModuleTypesTask
import modular.tasks.GenerateGraphvizFileTask
import modular.tasks.GenerateLegendDotFileTask
import org.gradle.api.Plugin
import org.gradle.api.Project
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
      val tasks = when (spec) {
        is DotFileSpec -> registerDotFileTasks(extension, spec)
      }
      generateLegend.get().dependsOn(tasks)
    }

    afterEvaluate {
      failIfInvalidColors(extension)
      warnIfSvgSelectedWithCustomDpi(extension)

      val types = extension.orderedTypes()
      warnIfNoModuleTypes(types)
      warnIfModuleTypesSpecifyNothing(types)
    }
  }

  private fun Project.registerDotFileTasks(
    extension: ModularExtensionImpl,
    spec: DotFileSpec,
  ): List<TaskProvider<GenerateGraphvizFileTask>> = spec.legend
    ?.let { legend ->
      // Only create a legend if one of the legend functions was explicitly called
      val dotFileTask = GenerateLegendDotFileTask.register(
        target = this,
        legendSpec = legend,
        spec = spec,
        extension = extension,
      )

      GenerateGraphvizFileTask.register(
        target = this,
        extension = extension,
        spec = spec,
        variant = Variant.Legend,
        dotFileTask = dotFileTask,
      )
    }.orEmpty()
}
