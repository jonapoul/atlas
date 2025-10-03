/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.AGP_VERSION
import modular.test.GraphvizScenario
import modular.test.KOTLIN_VERSION
import modular.test.androidBuildScript
import modular.test.javaBuildScript
import modular.test.kotlinJvmBuildScript

object ThreeModulesWithBuiltInTypes : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") version "$KOTLIN_VERSION" apply false
      kotlin("android") version "$KOTLIN_VERSION" apply false
      id("com.android.library") version "$AGP_VERSION" apply false
      id("$pluginId")
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
    "test-data" to javaBuildScript,
    "test-domain" to kotlinJvmBuildScript,
    "test-ui" to androidBuildScript,
  )
}
