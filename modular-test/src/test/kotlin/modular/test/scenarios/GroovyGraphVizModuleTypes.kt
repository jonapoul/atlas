/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
package modular.test.scenarios

import modular.test.Scenario

internal object GroovyGraphVizModuleTypes : Scenario by GroovyBasic {
  override val rootBuildFile = """
    plugins {
      id 'org.jetbrains.kotlin.jvm'
      id 'dev.jonpoulton.modular.graphviz'
    }

    modular {
      moduleTypes {
        kotlinJvm {
          color = "mediumorchid"
          hasPluginId = "org.jetbrains.kotlin.jvm"
        }

        other {
          color = "gainsboro"
          pathMatches = ".*?"
        }
      }
    }
  """.trimIndent()
}
