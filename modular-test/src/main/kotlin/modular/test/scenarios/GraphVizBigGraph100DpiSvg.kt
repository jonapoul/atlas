/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.KOTLIN_VERSION
import modular.test.Scenario
import kotlin.text.trimIndent

object GraphVizBigGraph100DpiSvg : Scenario by GraphVizBigGraph {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    modular {
      moduleTypes {
        kotlinJvm()
      }

      graphviz {
        dpi = 100
        fileFormat = "svg"
      }
    }
  """.trimIndent()
}
