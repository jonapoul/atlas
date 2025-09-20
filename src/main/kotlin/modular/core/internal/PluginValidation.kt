/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.core.internal

import modular.core.spec.ModuleType
import modular.graphviz.internal.GraphVizSpecImpl
import org.gradle.api.Project

internal fun Project.warnIfModuleTypesSpecifyNothing(types: List<ModuleType>) {
  types.forEach { type ->
    if (!type.pathContains.isPresent && !type.pathMatches.isPresent && !type.hasPluginId.isPresent) {
      logger.warn(
        "Warning: Module type '${type.name}' will be ignored - you need to set one of " +
          "pathContains, pathMatches or hasPluginId.",
      )
    }
  }
}

internal fun Project.warnIfSvgSelectedWithCustomDpi(extension: ModularExtensionImpl) {
  val graphVizSpec = extension.specs.filterIsInstance<GraphVizSpecImpl>().firstOrNull() ?: return
  val adjustSvgViewBox = graphVizSpec.adjustSvgViewBox.get()
  val warningIsSuppressed = extension.properties.suppressSvgViewBoxWarning.get()
  if (!adjustSvgViewBox && graphVizSpec.dpi.isPresent && !warningIsSuppressed) {
    val msg = "Configuring a custom DPI on a dotfile's with SVG output enabled will likely cause a misaligned " +
      "viewBox. Try adding the following property to your build file to automatically attempt a fix:"
    logger.warn(
      """
        $msg

          modular {
            experimental {
              adjustSvgViewBox = true
            }
          }

        or add "modular.suppress.adjustSvgViewBox=true" to your gradle.properties file to suppress this warning.
      """.trimIndent(),
    )
  }
}
