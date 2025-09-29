/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.GraphvizScenario
import modular.test.kotlinJvmBuildScript

object NoModuleTypesDeclared : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("$pluginId")
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "test-jvm" to kotlinJvmBuildScript,
  )
}
