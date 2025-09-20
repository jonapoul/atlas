/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object LeafDeclaredOnChildren : Scenario {
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
        id("dev.jonpoulton.modular.leaf")
      }
      dependencies {
        implementation(project(":b"))
        implementation(project(":c"))
      }
    """.trimIndent(),

    "b" to """
      plugins {
        kotlin("jvm")
        id("dev.jonpoulton.modular.leaf")
      }
    """.trimIndent(),

    "c" to """
      plugins {
        kotlin("jvm")
        id("dev.jonpoulton.modular.leaf")
      }
    """.trimIndent(),
  )
}
