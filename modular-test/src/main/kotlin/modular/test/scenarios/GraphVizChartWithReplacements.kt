/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario
import kotlin.text.trimIndent

object GraphVizChartWithReplacements : Scenario by GraphVizBasic {
  override val rootBuildFile = """
    import modular.graphviz.ArrowType
    import modular.graphviz.RankDir

    plugins {
      kotlin("jvm") apply false
      id("$pluginId")
    }

    modular {
      moduleTypes {
        kotlinJvm()
        java()
      }

      modulePathTransforms {
        replace(pattern = "^:", replacement = "") // remove ":" prefix
        replace(pattern = "^b$", replacement = "B") // rename one module to uppercase
      }
    }
  """.trimIndent()
}
