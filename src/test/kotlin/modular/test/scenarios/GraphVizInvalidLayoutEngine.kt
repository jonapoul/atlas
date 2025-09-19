/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object GraphVizInvalidLayoutEngine : Scenario by GraphVizBigGraph {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      moduleTypes {
        kotlinJvm()
      }

      graphViz {
        layoutEngine = "abc123"

        fileFormats {
          svg()
        }
      }
    }
  """.trimIndent()
}
