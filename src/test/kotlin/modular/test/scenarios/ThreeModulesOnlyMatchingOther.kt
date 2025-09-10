/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.BASIC_ANDROID_BUILD_SCRIPT
import modular.test.BASIC_JAVA_BUILD_SCRIPT
import modular.test.BASIC_JVM_BUILD_SCRIPT
import modular.test.Scenario

object ThreeModulesOnlyMatchingOther : Scenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      kotlin("android") apply false
      id("com.android.library") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      moduleTypes {
        registerByPluginId(name = "Won't match", color = "#123456", pluginId = "com.something.whatever")
        other()
      }
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "a" to BASIC_JAVA_BUILD_SCRIPT,
    "b" to BASIC_JVM_BUILD_SCRIPT,
    "c" to BASIC_ANDROID_BUILD_SCRIPT,
  )
}
