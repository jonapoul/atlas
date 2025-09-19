/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object GraphVizWithLinkTypes : Scenario by GraphVizBasic {
  override val rootBuildFile = """
    import modular.graphviz.spec.LinkStyle

    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      moduleTypes {
        kotlinJvm()
        java()
      }

      graphViz()

      linkTypes {
        "jvmMainImplementation"(style = LinkStyle.Bold, color = "orange")
        api()
        implementation(LinkStyle.Dotted)
      }
    }
  """.trimIndent()
}
