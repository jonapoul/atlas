/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object DotFileCustomLayoutEngine : Scenario by DotFileBigGraph {
  override val rootBuildFile = """
    import modular.spec.LayoutEngine.Neato

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
