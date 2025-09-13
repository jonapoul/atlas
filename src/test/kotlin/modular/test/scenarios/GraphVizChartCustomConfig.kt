/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object GraphVizChartCustomConfig : Scenario by GraphVizBasic {
  override val rootBuildFile = """
    import modular.graphviz.spec.ArrowType
    import modular.graphviz.spec.Dir
    import modular.graphviz.spec.RankDir

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

      graphViz {
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
