/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.test.scenarios

import atlas.test.KOTLIN_VERSION
import atlas.test.Scenario

internal object TriangleGraphWithUpwardsTraversal : Scenario by TriangleGraph {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      alsoTraverseUpwards = true

      moduleTypes {
        kotlinJvm()
      }
    }
  """.trimIndent()
}
