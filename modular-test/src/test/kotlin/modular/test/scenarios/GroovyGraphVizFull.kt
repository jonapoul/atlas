/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.GraphvizScenario

object GroovyGraphVizFull : GraphvizScenario by GroovyBasic {
  override val isGroovy = true

  override val rootBuildFile = """
    import modular.graphviz.*

    plugins {
      id 'org.jetbrains.kotlin.jvm'
      id 'dev.jonpoulton.modular.graphviz'
    }

    modular {
      graphviz {
        fileFormat = FileFormat.Svg
        layoutEngine = LayoutEngine.Circo
      }
    }
  """.trimIndent()
}
