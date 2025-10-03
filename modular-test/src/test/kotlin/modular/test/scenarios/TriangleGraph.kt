/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.GraphvizScenario
import modular.test.KOTLIN_VERSION
import modular.test.kotlinJvmBuildScript

object TriangleGraph : GraphvizScenario {
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
    "a" to """
      $kotlinJvmBuildScript
      dependencies {
        implementation(project(":b1"))
        implementation(project(":b2"))
      }
    """.trimIndent(),

    "b1" to """
      $kotlinJvmBuildScript
      dependencies {
        implementation(project(":c1"))
        implementation(project(":c2"))
      }
    """.trimIndent(),

    "b2" to """
      $kotlinJvmBuildScript
      dependencies {
        implementation(project(":c2"))
        implementation(project(":c3"))
      }
    """.trimIndent(),

    "c1" to kotlinJvmBuildScript,

    "c2" to kotlinJvmBuildScript,

    "c3" to kotlinJvmBuildScript,
  )
}
