/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.KOTLIN_VERSION
import modular.test.Scenario
import kotlin.text.trimIndent

internal object GraphVizWithLinkTypes : Scenario by GraphvizBasic {
  override val rootBuildFile = """
    import modular.graphviz.LinkStyle

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    modular {
      moduleTypes {
        kotlinJvm()
        java()
      }

      linkTypes {
        "jvmMainImplementation"(style = LinkStyle.Bold, color = "orange")
        api()
        implementation(LinkStyle.Dotted)
      }
    }
  """.trimIndent()
}
