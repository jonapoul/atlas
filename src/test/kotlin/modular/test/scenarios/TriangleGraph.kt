/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.BASIC_JVM_BUILD_SCRIPT
import modular.test.Scenario
import modular.test.settingsFileRepositories

object TriangleGraph : Scenario {
  override val settingsFile = """
    $settingsFileRepositories
    include(":a")
    include(":b1")
    include(":b2")
    include(":c1")
    include(":c2")
    include(":c3")
  """.trimIndent()

  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular")
    }

    modular {
      moduleTypes {
        kotlinJvm()
      }
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "a" to """
      $BASIC_JVM_BUILD_SCRIPT
      dependencies {
        implementation(project(":b1"))
        implementation(project(":b2"))
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
        implementation(project(":c2"))
        implementation(project(":c3"))
      }
    """.trimIndent(),

    "c1" to BASIC_JVM_BUILD_SCRIPT,
    "c2" to BASIC_JVM_BUILD_SCRIPT,
    "c3" to BASIC_JVM_BUILD_SCRIPT,
  )
}
