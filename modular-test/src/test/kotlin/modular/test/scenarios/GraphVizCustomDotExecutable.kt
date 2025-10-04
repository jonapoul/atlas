/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.KOTLIN_VERSION
import modular.test.Scenario
import kotlin.text.trimIndent

object GraphVizCustomDotExecutable : Scenario by GraphvizBasic {
  override val rootBuildFile = """
    import modular.graphviz.FileFormat
    import modular.graphviz.LayoutEngine.Neato

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
        pathToDotCommand = file("path/to/custom/dot").absolutePath
        fileFormat = FileFormat.Svg
      }
    }
  """.trimIndent()
}
