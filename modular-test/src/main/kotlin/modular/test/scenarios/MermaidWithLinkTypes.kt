/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object MermaidWithLinkTypes : Scenario by MermaidBasic {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular")
    }

    modular {
      linkTypes {
        api(color = "green")
        implementation(color = "#5555FF")
        "compileOnly"(style = "dotted", color = "yellow")
      }

      mermaid()
    }
  """.trimIndent()
}
