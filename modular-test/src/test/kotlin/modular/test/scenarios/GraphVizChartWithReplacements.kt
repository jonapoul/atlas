/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.KOTLIN_VERSION
import modular.test.Scenario
import kotlin.text.trimIndent

object GraphVizChartWithReplacements : Scenario by GraphvizBasic {
  override val rootBuildFile = """
    import modular.graphviz.ArrowType
    import modular.graphviz.RankDir

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    modular {
      moduleTypes {
        kotlinJvm()
        java()
      }

      pathTransforms {
        replace(pattern = "^:", replacement = "") // remove ":" prefix
        replace(pattern = "^b$", replacement = "B") // rename one module to uppercase
      }
    }
  """.trimIndent()
}
