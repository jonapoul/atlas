/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.BASIC_JVM_BUILD_SCRIPT
import modular.test.Scenario

object NestedModules : Scenario {
  override val rootBuildFile = """
    plugins {
      kotlin("jvm") apply false
      id("dev.jonpoulton.modular.trunk")
    }

    modular {
      moduleTypes.kotlinJvm()
      graphViz()
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "app" to BASIC_JVM_BUILD_SCRIPT + """
      dependencies {
        implementation(project(":ui:a"))
        implementation(project(":ui:b"))
        implementation(project(":ui:c"))
      }
    """.trimIndent(),

    "ui:a" to BASIC_JVM_BUILD_SCRIPT + """
      dependencies {
        implementation(project(":domain:a"))
      }
    """.trimIndent(),

    "ui:b" to BASIC_JVM_BUILD_SCRIPT + """
      dependencies {
        implementation(project(":domain:b"))
      }
    """.trimIndent(),

    "ui:c" to BASIC_JVM_BUILD_SCRIPT + """
      dependencies {
        implementation(project(":domain:a"))
        implementation(project(":domain:b"))
      }
    """.trimIndent(),

    "domain:a" to BASIC_JVM_BUILD_SCRIPT + """
      dependencies {
        implementation(project(":data:a"))
      }
    """.trimIndent(),

    "domain:b" to BASIC_JVM_BUILD_SCRIPT + """
      dependencies {
        implementation(project(":data:a"))
        implementation(project(":data:b"))
      }
    """.trimIndent(),

    "data:a" to BASIC_JVM_BUILD_SCRIPT,

    "data:b" to BASIC_JVM_BUILD_SCRIPT,
  )
}
