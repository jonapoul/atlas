/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object DiamondGraphWithUpwardsTraversal : Scenario by DiamondGraph {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      alsoTraverseUpwards = true
      moduleTypes {
        kotlinJvm()
      }
    }
  """.trimIndent()
}
