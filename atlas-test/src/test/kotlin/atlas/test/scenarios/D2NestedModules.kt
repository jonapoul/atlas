/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.test.scenarios

import atlas.test.D2Scenario
import atlas.test.KOTLIN_VERSION
import atlas.test.kotlinJvmBuildScript

internal object D2NestedModules : D2Scenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "path:to:my:module" to kotlinJvmBuildScript,
  )
}
