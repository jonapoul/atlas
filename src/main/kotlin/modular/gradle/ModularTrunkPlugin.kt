/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("unused") // public API

package modular.gradle

import modular.internal.HEX_COLOR_REGEX
import modular.internal.orderedTypes
import modular.spec.DotFileOutputSpec
import modular.spec.ModuleType
import modular.tasks.CollateModuleLinksTask
import modular.tasks.CollateModuleTypesTask
import modular.tasks.GenerateLegendDotFileTask
import org.codehaus.groovy.syntax.Types.REGEX_PATTERN
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create

class ModularTrunkPlugin : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    val extension = extensions.create<ModularExtension>(ModularExtension.NAME)

    CollateModuleTypesTask.register(project, extension)
    CollateModuleLinksTask.register(project, extension)

    extension.outputs.configureEach { outputConfig ->
      when (outputConfig) {
        is DotFileOutputSpec -> GenerateLegendDotFileTask.register(
          target = project,
          config = outputConfig,
          extension = extension,
        )

        else -> error("Unknown output config $outputConfig")
      }
    }

    //      GeneratePngFileTask.registerLegend(project, generateLegend)

    afterEvaluate {
      applyLeavesIfConfigured(extension)
      failIfInvalidColors(extension)

      val types = extension.orderedTypes()
      warnIfNoModuleTypes(types)
      warnIfModuleTypesSpecifyNothing(types)
    }
  }

  private fun Project.applyLeavesIfConfigured(extension: ModularExtension) {
    if (extension.autoApplyLeaves.get()) {
      subprojects { p ->
        p.pluginManager.apply("dev.jonpoulton.modular.leaf")
      }
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
}
