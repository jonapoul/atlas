/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.BASIC_JVM_BUILD_SCRIPT
import modular.test.Scenario

object NoModuleTypesDeclared : Scenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "test-jvm" to BASIC_JVM_BUILD_SCRIPT,
  )
}
