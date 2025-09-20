/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object GraphVizCustomDotExecutable : Scenario by GraphVizBasic {
  override val rootBuildFile = """
    import modular.graphviz.spec.LayoutEngine.Neato

    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      moduleTypes {
        kotlinJvm()
        java()
      }

      graphViz {
        pathToDotCommand = file("path/to/custom/dot").absolutePath
        fileFormat = "svg"
        writeReadme = true
      }
    }
  """.trimIndent()
}
