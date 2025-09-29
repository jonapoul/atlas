/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario
import kotlin.text.trimIndent

object GraphVizInvalidLayoutEngine : Scenario by GraphVizBigGraph {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("$pluginId")
    }

    modular {
      moduleTypes {
        kotlinJvm()
      }

      graphViz {
        layoutEngine = "abc123"
        fileFormat = "svg"
      }
    }
  """.trimIndent()
}
