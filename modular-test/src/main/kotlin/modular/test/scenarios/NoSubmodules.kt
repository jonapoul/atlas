/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.GraphvizScenario

object NoSubmodules : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("$pluginId")
    }

    modular {
      moduleTypes {
        androidApp()
        kotlinMultiplatform()
        androidLibrary()
      }
    }
  """.trimIndent()
}
