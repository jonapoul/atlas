/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object GraphVizLegendCustomConfig : Scenario by GraphVizBasic {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      moduleTypes {
        kotlinJvm()
        java()
        registerByPluginId(name = "Custom", color = "#123456", pluginId = "com.something.whatever")
      }

      outputs {
        legendRootFilename = "legend-filename"
        saveLegendsRelativeToRootModule("legend-dir")
      }

      graphViz {
        legend {
          cellBorder = 2
          cellPadding = 3
          cellSpacing = 4
          tableBorder = 5
        }
      }
    }
  """.trimIndent()
}
