/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.GraphvizScenario

object GroovyBasic : GraphvizScenario {
  override val isGroovy = true

  override val rootBuildFile = """
    plugins {
      id 'org.jetbrains.kotlin.jvm'
      id '$pluginId'
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "a" to """
      plugins {
        id 'org.jetbrains.kotlin.jvm'
      }

      dependencies {
        api(project(':b'))
        implementation(project(':c'))
      }
    """.trimIndent(),

    "b" to """
      plugins {
        id 'org.jetbrains.kotlin.jvm'
      }
    """.trimIndent(),

    "c" to """
      plugins {
        id 'java'
      }
    """.trimIndent(),
  )
}
