/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.KOTLIN_VERSION
import modular.test.Scenario

object GraphVizBasicWithPngOutput : Scenario by GraphvizBasic {
  override val rootBuildFile = """
    import modular.graphviz.FileFormat

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    modular {
      moduleTypes {
        kotlinJvm()
        java()
      }

      graphviz {
        fileFormat = FileFormat.Png
      }
    }
  """.trimIndent()
}
