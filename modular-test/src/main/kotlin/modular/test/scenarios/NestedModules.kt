/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.GraphvizScenario
import modular.test.Scenario
import modular.test.kotlinJvmBuildScript

object NestedModules : GraphvizScenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("$pluginId")
    }

    modular {
      moduleTypes.kotlinJvm()
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "app" to kotlinJvmBuildScript + """
      dependencies {
        implementation(project(":ui:a"))
        implementation(project(":ui:b"))
        implementation(project(":ui:c"))
      }
    """.trimIndent(),

    "ui:a" to kotlinJvmBuildScript + """
      dependencies {
        implementation(project(":domain:a"))
      }
    """.trimIndent(),

    "ui:b" to kotlinJvmBuildScript + """
      dependencies {
        implementation(project(":domain:b"))
      }
    """.trimIndent(),

    "ui:c" to kotlinJvmBuildScript + """
      dependencies {
        implementation(project(":domain:a"))
        implementation(project(":domain:b"))
      }
    """.trimIndent(),

    "domain:a" to kotlinJvmBuildScript + """
      dependencies {
        implementation(project(":data:a"))
      }
    """.trimIndent(),

    "domain:b" to kotlinJvmBuildScript + """
      dependencies {
        implementation(project(":data:a"))
        implementation(project(":data:b"))
      }
    """.trimIndent(),

    "data:a" to kotlinJvmBuildScript,

    "data:b" to kotlinJvmBuildScript,
  )
}
