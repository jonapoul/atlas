/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.GraphvizScenario
import modular.test.kotlinJvmBuildScript

object GraphVizBigGraph : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("$pluginId")
    }

    modular {
      moduleTypes{
        kotlinJvm()
      }
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "app" to """
      $kotlinJvmBuildScript
      dependencies {
        implementation(project(":a1"))
        implementation(project(":a2"))
        implementation(project(":a3"))
      }
    """.trimIndent(),

    "a1" to """
      $kotlinJvmBuildScript
      dependencies {
        implementation(project(":b1"))
        implementation(project(":b2"))
      }
    """.trimIndent(),

    "a2" to """
      $kotlinJvmBuildScript
      dependencies {
        implementation(project(":b2"))
        implementation(project(":b3"))
      }
    """.trimIndent(),

    "a3" to """
      $kotlinJvmBuildScript
      dependencies {
        implementation(project(":b3"))
        implementation(project(":b4"))
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
        implementation(project(":c1"))
        implementation(project(":c2"))
      }
    """.trimIndent(),

    "b3" to """
      $kotlinJvmBuildScript
      dependencies {
        implementation(project(":c1"))
        implementation(project(":c4"))
      }
    """.trimIndent(),

    "b4" to """
      $kotlinJvmBuildScript
      dependencies {
        implementation(project(":c3"))
        implementation(project(":c4"))
        implementation(project(":c5"))
      }
    """.trimIndent(),

    "c1" to kotlinJvmBuildScript,
    "c2" to kotlinJvmBuildScript,
    "c3" to kotlinJvmBuildScript,
    "c4" to kotlinJvmBuildScript,
    "c5" to kotlinJvmBuildScript,
  )
}
