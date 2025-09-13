/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.BASIC_JAVA_BUILD_SCRIPT
import modular.test.BASIC_JVM_BUILD_SCRIPT
import modular.test.Scenario

object GraphVizBasic : Scenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      moduleTypes {
        kotlinJvm()
        java()
        registerByPluginId(name = "Custom", color = "#123456", pluginId = "com.something.whatever")
      }

      graphViz()
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "a" to """
      $BASIC_JVM_BUILD_SCRIPT
      dependencies {
        api(project(":b"))
        implementation(project(":c"))
      }
    """.trimIndent(),

    "b" to BASIC_JAVA_BUILD_SCRIPT,

    "c" to BASIC_JAVA_BUILD_SCRIPT,
  )
}
