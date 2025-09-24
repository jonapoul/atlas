/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.gradle.ModularExtension
import modular.graphviz.internal.GraphvizSpecImpl
import org.gradle.api.Project

internal fun Project.warnIfModuleTypesSpecifyNothing(extension: ModularExtension) {
  extension.orderedModuleTypes().forEach { type ->
    if (!type.pathContains.isPresent && !type.pathMatches.isPresent && !type.hasPluginId.isPresent) {
      logger.warn(
        "Warning: Module type '${type.name}' will be ignored - you need to set one of " +
          "pathContains, pathMatches or hasPluginId.",
      )
    }
  }
}

internal fun Project.warnIfSvgSelectedWithCustomDpi(extension: ModularExtensionImpl) {
  val graphVizSpec = extension.specs.filterIsInstance<GraphvizSpecImpl>().firstOrNull() ?: return
  val adjustSvgViewBox = graphVizSpec.adjustSvgViewBox.get()
  val warningIsSuppressed = extension.properties.graphviz.suppressSvgViewBoxWarning
    .get()
  if (!adjustSvgViewBox && graphVizSpec.dpi.isPresent && !warningIsSuppressed) {
    val msg = "Configuring a custom DPI on a dotfile's with SVG output enabled will likely cause a misaligned " +
      "viewBox. Try adding the following property to your build file to automatically attempt a fix:"
    logger.warn(
      """
        $msg

          modular {
            graphviz {
              adjustSvgViewBox = true
            }
          }

        or add "modular.graphviz.suppressAdjustSvgViewBox=true" to your gradle.properties file to suppress this warning.
      """.trimIndent(),
    )
  }
}
