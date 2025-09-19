/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object GraphVizCustomLayoutEngine : Scenario by GraphVizBigGraph {
  override val rootBuildFile = """
    import modular.graphviz.spec.LayoutEngine.Neato

    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      moduleTypes {
        kotlinJvm()
      }

      graphViz {
        chart {
          layoutEngine(Neato)
        }

        fileFormats {
          svg()
        }
      }
    }
  """.trimIndent()
}
