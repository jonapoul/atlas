/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.KOTLIN_VERSION
import atlas.test.kotlinJvmBuildScript

internal object ModuleTypesDeclaredButNoneMatch : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    atlas {
      moduleTypes {
        androidApp()
        kotlinMultiplatform()
        androidLibrary()
      }
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "test-jvm" to kotlinJvmBuildScript,
  )
}
