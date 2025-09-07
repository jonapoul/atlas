/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.BASIC_ANDROID_BUILD_SCRIPT
import modular.test.BASIC_JAVA_BUILD_SCRIPT
import modular.test.BASIC_JVM_BUILD_SCRIPT
import modular.test.Scenario
import modular.test.settingsFileRepositories

object ThreeModulesWithBuiltInTypes : Scenario {
  override val settingsFile = """
    $settingsFileRepositories
    include(":test-data")
    include(":test-domain")
    include(":test-ui")
  """.trimIndent()

  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      kotlin("android") apply false
      id("com.android.library") apply false
      id("dev.jonpoulton.modular")
    }

    modular {
      moduleTypes {
        androidLibrary()
        kotlinJvm()
        java()
      }
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "test-data" to BASIC_JAVA_BUILD_SCRIPT,
    "test-domain" to BASIC_JVM_BUILD_SCRIPT,
    "test-ui" to BASIC_ANDROID_BUILD_SCRIPT,
  )
}
