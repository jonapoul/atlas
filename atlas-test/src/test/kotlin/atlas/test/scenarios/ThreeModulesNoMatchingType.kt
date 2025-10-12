/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.test.scenarios

import atlas.test.AGP_VERSION
import atlas.test.GraphvizScenario
import atlas.test.KOTLIN_VERSION
import atlas.test.androidBuildScript
import atlas.test.javaBuildScript
import atlas.test.kotlinJvmBuildScript

internal object ThreeModulesNoMatchingType : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      kotlin("android") version "$KOTLIN_VERSION" apply false
      id("com.android.library") version "$AGP_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      moduleTypes {
        registerByPluginId(name = "Won't match", color = "#123456", pluginId = "com.something.whatever")
      }
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "a" to javaBuildScript,
    "b" to kotlinJvmBuildScript,
    "c" to androidBuildScript,
  )
}
