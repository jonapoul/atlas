/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.BASIC_JVM_BUILD_SCRIPT
import modular.test.Scenario
import modular.test.settingsFileRepositories

object DiamondGraph : Scenario {
  override val settingsFile = """
    $settingsFileRepositories
    include(":top")
    include(":mid-a")
    include(":mid-b")
    include(":bottom")
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
    "top" to """
      $BASIC_JVM_BUILD_SCRIPT
      dependencies {
        api(project(":mid-a"))
        implementation(project(":mid-b"))
      }
    """.trimIndent(),

    "mid-a" to """
      $BASIC_JVM_BUILD_SCRIPT
      dependencies {
        api(project(":bottom"))
      }
    """.trimIndent(),

    "mid-b" to """
      $BASIC_JVM_BUILD_SCRIPT
      dependencies {
        implementation(project(":bottom"))
      }
    """.trimIndent(),

    "bottom" to BASIC_JVM_BUILD_SCRIPT,
  )
}
