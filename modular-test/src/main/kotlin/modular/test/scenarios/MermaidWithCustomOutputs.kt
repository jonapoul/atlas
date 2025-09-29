/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object MermaidWithCustomOutputs : Scenario by MermaidBasic {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular")
    }

    modular {
      mermaid()

      outputs {
        saveChartsRelativeToSubmodule("charts")
        saveLegendsRelativeToRootModule("legend")
        legendRootFilename = "legend-custom"
        chartRootFilename = "chart-custom"
      }
    }
  """.trimIndent()
}
