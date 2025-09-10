/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.gradle

import modular.internal.HEX_COLOR_REGEX
import modular.internal.MODULAR_TASK_GROUP
import modular.internal.configureSeparators
import modular.internal.orderedTypes
import modular.internal.registerGenerationTaskOnSync
import modular.spec.DotFileSpec
import modular.spec.ModuleType
import modular.tasks.CollateModuleLinksTask
import modular.tasks.CollateModuleTypesTask
import modular.tasks.GenerateGraphvizFileTask
import modular.tasks.GenerateLegendDotFileTask
import org.codehaus.groovy.syntax.Types.REGEX_PATTERN
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider

class ModularTrunkPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    if (target != rootProject) {
      error("ModularTrunkPlugin should only be applied on the root project - you applied it to $path")
    }

    val extension = extensions.create(ModularExtension.NAME, ModularExtension::class.java)
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

      val types = extension.orderedTypes()
      warnIfNoModuleTypes(types)
      warnIfModuleTypesSpecifyNothing(types)
    }
  }

  private fun failIfInvalidColors(extension: ModularExtension) {
    extension.moduleTypes.configureEach { type ->
      val color = type.color.get()
      if (!color.matches(HEX_COLOR_REGEX)) {
        error("Invalid color string '$color' - should match regex pattern '$REGEX_PATTERN'")
      }
    }
  }

  private fun Project.warnIfNoModuleTypes(types: List<ModuleType>) {
    if (types.isEmpty()) {
      logger.warn("Warning: No module types have been registered!")
    }
  }

  private fun Project.warnIfModuleTypesSpecifyNothing(types: List<ModuleType>) {
    types.forEach { type ->
      if (!type.pathContains.isPresent && !type.pathMatches.isPresent && !type.hasPluginId.isPresent) {
        logger.warn(
          "Warning: Module type '${type.name}' will be ignored - you need to set one of " +
            "pathContains, pathMatches or hasPluginId.",
        )
      }
    }
  }

  private fun Project.registerDotFileTasks(
    extension: ModularExtension,
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
        output = extension.outputs,
        spec = spec,
        variant = Variant.Legend,
        dotFileTask = dotFileTask,
      )
    }.orEmpty()
}
