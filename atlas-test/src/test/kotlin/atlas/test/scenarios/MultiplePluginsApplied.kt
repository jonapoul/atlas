/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package atlas.test.scenarios

import atlas.test.GraphvizScenario
import atlas.test.KOTLIN_VERSION

internal object MultiplePluginsApplied : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("dev.jonpoulton.atlas.d2")
      id("dev.jonpoulton.atlas.mermaid")
      id("dev.jonpoulton.atlas.graphviz")
    }
  """.trimIndent()

  override val submoduleBuildFiles = emptyMap<String, String>()
}
