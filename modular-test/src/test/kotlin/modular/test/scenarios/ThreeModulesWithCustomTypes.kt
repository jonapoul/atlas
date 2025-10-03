/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.AGP_VERSION
import modular.test.GraphvizScenario
import modular.test.KOTLIN_VERSION
import modular.test.androidBuildScript
import modular.test.kotlinJvmBuildScript

object ThreeModulesWithCustomTypes : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      kotlin("android") version "$KOTLIN_VERSION" apply false
      id("com.android.library") version "$AGP_VERSION" apply false
      id("$pluginId")
    }

    modular {
      moduleTypes {
        create("Data") {
          color = "#ABC123"
          pathContains = "data"
        }
        create("Domain") {
          color = "#123ABC"
          pathMatches = ".*-domain"
        }
        create("Android") {
          color = "#A1B2C3"
          hasPluginId = "com.android.base"
        }
      }
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "test-data" to kotlinJvmBuildScript,
    "test-domain" to kotlinJvmBuildScript,
    "test-ui" to androidBuildScript,
  )
}
