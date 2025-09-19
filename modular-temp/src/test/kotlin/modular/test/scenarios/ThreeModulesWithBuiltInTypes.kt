/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.BASIC_ANDROID_BUILD_SCRIPT
import modular.test.BASIC_JAVA_BUILD_SCRIPT
import modular.test.BASIC_JVM_BUILD_SCRIPT
import modular.test.Scenario

object ThreeModulesWithBuiltInTypes : Scenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      kotlin("android") apply false
      id("com.android.library") apply false
      id("dev.jonpoulton.modular.trunk")
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
