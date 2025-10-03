/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.KOTLIN_VERSION
import modular.test.Scenario
import kotlin.text.trimIndent

object GraphVizChartCustomConfig : Scenario by GraphVizBasic {
  override val rootBuildFile = """
    import modular.graphviz.ArrowType
    import modular.graphviz.Dir
    import modular.graphviz.RankDir

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
        arrowHead = "halfopen"
        arrowTail(ArrowType.Open)
        dpi = 150
        fontSize = 20
        rankDir(RankDir.LeftToRight)
        rankSep = 2.5f
        dir(Dir.None)
      }
    }
  """.trimIndent()
}
