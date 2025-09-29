/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario
import kotlin.text.trimIndent

object GraphVizBigGraph100DpiSvgWithAdjustment : Scenario by GraphVizBigGraph {
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
        adjustSvgViewBox = true
        dpi = 100
        fileFormat = "svg"
      }
    }
  """.trimIndent()
}
