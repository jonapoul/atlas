/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object LeafUndeclaredOnChildren : Scenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "a" to """
      plugins {
        kotlin("jvm")
      }
      dependencies {
        implementation(project(":b"))
        implementation(project(":c"))
      }
    """.trimIndent(),

    "b" to """
      plugins {
        kotlin("jvm")
      }
    """.trimIndent(),

    "c" to """
      plugins {
        kotlin("jvm")
      }
    """.trimIndent(),
  )
}
