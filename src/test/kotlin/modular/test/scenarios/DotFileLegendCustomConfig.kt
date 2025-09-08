/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object DotFileLegendCustomConfig : Scenario by DotFileBasic {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular")
    }

    modular {
      moduleTypes {
        kotlinJvm()
        java()
        registerByPluginId(name = "Custom", color = "#123456", pluginId = "com.something.whatever")
      }

      dotFile {
        legend {
          file = file("custom-legend.dot")
          cellBorder = 2
          cellPadding = 3
          cellSpacing = 4
          tableBorder = 5
        }
      }
    }
  """.trimIndent()
}
