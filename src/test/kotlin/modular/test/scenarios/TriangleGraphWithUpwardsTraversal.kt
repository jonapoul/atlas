/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object TriangleGraphWithUpwardsTraversal : Scenario by TriangleGraph {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      general {
        supportUpwardsTraversal = true
      }

      moduleTypes {
        kotlinJvm()
      }
    }
  """.trimIndent()
}
