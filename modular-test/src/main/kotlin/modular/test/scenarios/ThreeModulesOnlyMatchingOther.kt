/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.GraphvizScenario
import modular.test.androidBuildScript
import modular.test.javaBuildScript
import modular.test.kotlinJvmBuildScript

object ThreeModulesOnlyMatchingOther : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      kotlin("android") apply false
      id("com.android.library") apply false
      id("$pluginId")
    }

    modular {
      moduleTypes {
        registerByPluginId(name = "Won't match", color = "#123456", pluginId = "com.something.whatever")
        other()
      }
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "a" to javaBuildScript,
    "b" to kotlinJvmBuildScript,
    "c" to androidBuildScript,
  )
}
