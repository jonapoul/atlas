/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.GraphvizScenario
import modular.test.KOTLIN_VERSION
import modular.test.kotlinJvmBuildScript

internal object DiamondGraph : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      id("$pluginId")
    }

    modular {
      moduleTypes {
        kotlinJvm()
      }
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "top" to """
      $kotlinJvmBuildScript
      dependencies {
        api(project(":mid-a"))
        implementation(project(":mid-b"))
      }
    """.trimIndent(),

    "mid-a" to """
      $kotlinJvmBuildScript
      dependencies {
        api(project(":bottom"))
      }
    """.trimIndent(),

    "mid-b" to """
      $kotlinJvmBuildScript
      dependencies {
        implementation(project(":bottom"))
      }
    """.trimIndent(),

    "bottom" to kotlinJvmBuildScript,
  )
}
