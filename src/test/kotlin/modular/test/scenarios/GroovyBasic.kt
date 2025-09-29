/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

object GroovyBasic : Scenario {
  override val isGroovy = true

  override val rootBuildFile = """
    plugins {
      id 'org.jetbrains.kotlin.jvm'
      id 'dev.jonpoulton.modular.trunk'
    }
  """.trimIndent()

  override val submoduleBuildFiles = mapOf(
    "a" to """
      plugins {
        id 'org.jetbrains.kotlin.jvm'
        id 'dev.jonpoulton.modular.leaf'
      }

      dependencies {
        api(project(':b'))
        implementation(project(':c'))
      }
    """.trimIndent(),

    "b" to """
      plugins {
        id 'org.jetbrains.kotlin.jvm'
        id 'dev.jonpoulton.modular.leaf'
      }
    """.trimIndent(),

    "c" to """
      plugins {
        id 'java'
        id 'dev.jonpoulton.modular.leaf'
      }
    """.trimIndent(),
  )
}
