/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.BASIC_JVM_BUILD_SCRIPT
import modular.test.Scenario

object DotFileBigGraph : Scenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      moduleTypes{
        kotlinJvm()
      }

      dotFile()
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "app" to """
      $BASIC_JVM_BUILD_SCRIPT
      dependencies {
        implementation(project(":a1"))
        implementation(project(":a2"))
        implementation(project(":a3"))
      }
    """.trimIndent(),

    "a1" to """
      $BASIC_JVM_BUILD_SCRIPT
      dependencies {
        implementation(project(":b1"))
        implementation(project(":b2"))
      }
    """.trimIndent(),

    "a2" to """
      $BASIC_JVM_BUILD_SCRIPT
      dependencies {
        implementation(project(":b2"))
        implementation(project(":b3"))
      }
    """.trimIndent(),

    "a3" to """
      $BASIC_JVM_BUILD_SCRIPT
      dependencies {
        implementation(project(":b3"))
        implementation(project(":b4"))
      }
    """.trimIndent(),

    "b1" to """
      $BASIC_JVM_BUILD_SCRIPT
      dependencies {
        implementation(project(":c1"))
        implementation(project(":c2"))
      }
    """.trimIndent(),

    "b2" to """
      $BASIC_JVM_BUILD_SCRIPT
      dependencies {
        implementation(project(":c1"))
        implementation(project(":c2"))
      }
    """.trimIndent(),

    "b3" to """
      $BASIC_JVM_BUILD_SCRIPT
      dependencies {
        implementation(project(":c1"))
        implementation(project(":c4"))
      }
    """.trimIndent(),

    "b4" to """
      $BASIC_JVM_BUILD_SCRIPT
      dependencies {
        implementation(project(":c3"))
        implementation(project(":c4"))
        implementation(project(":c5"))
      }
    """.trimIndent(),

    "c1" to BASIC_JVM_BUILD_SCRIPT,
    "c2" to BASIC_JVM_BUILD_SCRIPT,
    "c3" to BASIC_JVM_BUILD_SCRIPT,
    "c4" to BASIC_JVM_BUILD_SCRIPT,
    "c5" to BASIC_JVM_BUILD_SCRIPT,
  )
}
