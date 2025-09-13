/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object DotFileCustomDotExecutable : Scenario by DotFileBasic {
  override val rootBuildFile = """
    import modular.spec.LayoutEngine.Neato

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
        fileFormats.svg()
      }
    }
  """.trimIndent()
}
