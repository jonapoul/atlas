/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.BASIC_ANDROID_BUILD_SCRIPT
import modular.test.BASIC_JVM_BUILD_SCRIPT
import modular.test.Scenario

object ThreeModulesWithCustomTypes : Scenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      kotlin("android") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      moduleTypes {
        create("Data") {
          color = "#ABC123"
          pathContains = "data"
        }
        create("Domain") {
          color = "#123ABC"
          pathMatches = ".*-domain".toRegex()
        }
        create("Android") {
          color = "#A1B2C3"
          hasPluginId = "com.android.base"
        }
      }
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "test-data" to BASIC_JVM_BUILD_SCRIPT,
    "test-domain" to BASIC_JVM_BUILD_SCRIPT,
    "test-ui" to BASIC_ANDROID_BUILD_SCRIPT,
  )
}
