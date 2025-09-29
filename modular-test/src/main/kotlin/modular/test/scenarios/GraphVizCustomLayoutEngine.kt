/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario
import kotlin.text.trimIndent

object GraphVizCustomLayoutEngine : Scenario by GraphVizBigGraph {
  override val rootBuildFile = """
    import modular.graphviz.LayoutEngine.Neato

    plugins {
      kotlin("jvm") apply false
      id("$pluginId")
    }

    modular {
      moduleTypes {
        kotlinJvm()
      }

      graphViz {
        layoutEngine(Neato)
        fileFormat = "svg"
      }
    }
  """.trimIndent()
}
