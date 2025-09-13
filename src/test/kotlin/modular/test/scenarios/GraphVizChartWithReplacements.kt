/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object GraphVizChartWithReplacements : Scenario by GraphVizBasic {
  override val rootBuildFile = """
    import modular.spec.ArrowType
    import modular.spec.RankDir

    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
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

      graphViz()
    }
  """.trimIndent()
}
