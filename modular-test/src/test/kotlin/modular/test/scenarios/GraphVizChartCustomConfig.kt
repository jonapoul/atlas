/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.KOTLIN_VERSION
import modular.test.Scenario
import kotlin.text.trimIndent

internal object GraphVizChartCustomConfig : Scenario by GraphvizBasic {
  override val rootBuildFile = """
    import modular.graphviz.ArrowType
    import modular.graphviz.Dir
    import modular.graphviz.FileFormat
    import modular.graphviz.LayoutEngine
    import modular.graphviz.RankDir
    import modular.graphviz.Shape

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    modular {
      moduleTypes {
        kotlinJvm()
        java()
        registerByPluginId(name = "Custom", color = "#123456", pluginId = "com.something.whatever")
      }

      graphviz {
        fileFormat = FileFormat.Gif
        layoutEngine = LayoutEngine.TwoPi

        edge {
          arrowHead = ArrowType.HalfOpen
          arrowTail = ArrowType.Open
        }

        graph {
          dpi = 150
        }

        node {
          shape = Shape.None
        }
      }
    }
  """.trimIndent()
}
