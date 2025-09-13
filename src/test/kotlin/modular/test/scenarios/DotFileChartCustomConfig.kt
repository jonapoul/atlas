/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object DotFileChartCustomConfig : Scenario by DotFileBasic {
  override val rootBuildFile = """
    import modular.spec.ArrowType
    import modular.spec.Dir
    import modular.spec.RankDir

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

      dotFile {
        chart {
          arrowHead = "halfopen"
          arrowTail(ArrowType.Open)
          dpi = 150
          fontSize = 20
          rankDir(RankDir.LeftToRight)
          rankSep = 2.5f
          dir(Dir.None)
        }
      }
    }
  """.trimIndent()
}
