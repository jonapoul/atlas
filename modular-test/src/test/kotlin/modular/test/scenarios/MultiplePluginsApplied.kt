/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.GraphvizScenario
import modular.test.KOTLIN_VERSION

object MultiplePluginsApplied : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("dev.jonpoulton.modular.d2")
      id("dev.jonpoulton.modular.mermaid")
      id("dev.jonpoulton.modular.graphviz")
    }
  """.trimIndent()

  override val submoduleBuildFiles = emptyMap<String, String>()
}
