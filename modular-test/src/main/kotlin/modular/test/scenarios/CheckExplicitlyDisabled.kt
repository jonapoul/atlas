/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object CheckExplicitlyDisabled : Scenario by GraphVizBasic {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("$pluginId")
    }

    modular {
      moduleTypes {
        kotlinJvm()
        java()
      }

      checkOutputs = false
    }
  """.trimIndent()
}
