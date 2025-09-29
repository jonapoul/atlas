/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object GroovyGraphVizFull : Scenario by GroovyBasic {
  override val isGroovy = true

  override val rootBuildFile = """
    import modular.graphviz.spec.*

    plugins {
      id 'org.jetbrains.kotlin.jvm'
      id 'dev.jonpoulton.modular.trunk'
    }

    modular {
      graphViz {
        writeReadme = true
        adjustSvgViewBox = true
        pathToDotCommand = "dot"
        fileFormat('svg')
        arrowHead('halfopen')
        arrowTail('diamond')
        layoutEngine('circo')
        dpi = 123
        fontSize = 40
        rankDir(RankDir.TopToBottom)
        rankSep = 1.2f
        dir(Dir.Forward)
      }
    }
  """.trimIndent()
}
