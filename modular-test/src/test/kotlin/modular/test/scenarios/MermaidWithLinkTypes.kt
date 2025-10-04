/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.KOTLIN_VERSION
import modular.test.Scenario

object MermaidWithLinkTypes : Scenario by MermaidBasic {
  override val rootBuildFile = """
    import modular.core.LinkStyle

    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    modular {
      linkTypes {
        api(color = "green")
        implementation(color = "#5555FF")
        "compileOnly"(style = LinkStyle.Dotted, color = "yellow")
      }
    }
  """.trimIndent()
}
