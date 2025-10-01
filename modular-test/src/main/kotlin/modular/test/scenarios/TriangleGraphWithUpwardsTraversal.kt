/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.KOTLIN_VERSION
import modular.test.Scenario

object TriangleGraphWithUpwardsTraversal : Scenario by TriangleGraph {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    modular {
      alsoTraverseUpwards = true

      moduleTypes {
        kotlinJvm()
      }
    }
  """.trimIndent()
}
