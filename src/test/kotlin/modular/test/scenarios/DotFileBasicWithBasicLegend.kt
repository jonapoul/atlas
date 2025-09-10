/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object DotFileBasicWithBasicLegend : Scenario by DotFileBasic {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      moduleTypes {
        kotlinJvm()
        java()
        registerByPluginId(name = "Custom", color = "#123456", pluginId = "com.something.whatever")
      }

      dotFile {
        legend()
      }
    }
  """.trimIndent()
}
