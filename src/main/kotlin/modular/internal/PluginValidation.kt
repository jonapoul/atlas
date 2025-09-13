/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.internal

import modular.graphviz.spec.GraphVizSpec
import modular.spec.ModuleType
import org.gradle.api.Project

internal fun Project.warnIfNoModuleTypes(types: List<ModuleType>) {
  if (types.isEmpty()) {
    logger.warn("Warning: No module types have been registered!")
  }
}

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

internal fun Project.warnIfNoGraphVizOutputs(extension: ModularExtensionImpl) {
  val spec = extension.specs.filterIsInstance<GraphVizSpec>().firstOrNull() ?: return
  val fileFormats = spec.fileFormats.orNull
  val isSuppressed = extension.properties.suppressNoGraphVizOutputs.get()

  if (fileFormats != null && fileFormats.isEmpty() && !isSuppressed) {
    logger.warn(
      """
        No file formats have been registered as GraphViz outputs! Configure them from your build script like:

          modular {
            graphViz {
             fileFormats {
               png()
               svg()
               // etc.
             }
            }
          }

        You can suppress this warning by adding the Gradle property "modular.suppress.noGraphVizOutputs=true".
      """.trimIndent(),
    )
  }
}

internal fun Project.warnIfSvgSelectedWithCustomDpi(extension: ModularExtensionImpl) {
  val adjustSvgViewBox = extension.experimental.adjustSvgViewBox.get()
  val graphVizSpec = extension.specs.filterIsInstance<GraphVizSpec>().firstOrNull()
  val warningIsSuppressed = extension.properties.suppressSvgViewBoxWarning.get()
  val dpiIsConfigured = graphVizSpec?.chart?.dpi?.isPresent == true
  if (!adjustSvgViewBox && dpiIsConfigured && !warningIsSuppressed) {
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
